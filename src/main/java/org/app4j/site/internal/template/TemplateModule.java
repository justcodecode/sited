package org.app4j.site.internal.template;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.app4j.site.Site;
import org.app4j.site.internal.InternalModule;
import org.app4j.site.internal.cache.CacheModule;
import org.app4j.site.internal.error.ErrorModule;
import org.app4j.site.internal.route.RouteModule;
import org.app4j.site.internal.template.processor.LangAttrProcessor;
import org.app4j.site.internal.template.processor.TemplateHrefAttrProcessor;
import org.app4j.site.internal.template.processor.TemplateSrcAttrProcessor;
import org.app4j.site.internal.template.service.TemplateDialect;
import org.app4j.site.internal.template.service.TemplateRepository;
import org.app4j.site.internal.template.web.AssetsHandler;
import org.app4j.site.util.FolderResourceRepository;
import org.app4j.site.util.SortedList;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.BadRequestException;
import org.app4j.site.web.exception.NotFoundException;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.StandardCacheManager;
import org.thymeleaf.context.IContext;
import org.thymeleaf.resource.IResource;
import org.thymeleaf.resource.StringResource;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class TemplateModule extends InternalModule implements TemplateConfig {
    private final TemplateDialect templateDialect = new TemplateDialect();
    private final TemplateResolver templateResolver = new TemplateResolver();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private final StandardCacheManager cacheManager = new StandardCacheManager();
    private final SortedList<TemplateRepository> templateRepositories = new SortedList<>((o1, o2) -> o2.priority() - o1.priority());
    private final File dir;
    private final AssetsConfig assetsConfig = new AssetsConfig();


    public TemplateModule(Site site) {
        super(site);
        dependencies.addAll(Arrays.asList(RouteModule.class, CacheModule.class,
            ErrorModule.class));

        this.dir = site.dir("web");
        templateEngine.setCacheManager(cacheManager);
        templateEngine.setDialect(templateDialect);

        templateResolver.setCharacterEncoding(Charsets.UTF_8.name());
        templateResolver.setResourceResolver(new IResourceResolver() {
            @Override
            public String getName() {
                return "template";
            }

            @Override
            public IResource resolveResource(IEngineConfiguration configuration, IContext context, String resource, String characterEncoding) {
                Template template = get(resource).get();
                return new StringResource(template.path(), template.text());
            }
        });
    }

    @Override
    public Optional<Template> get(String templatePath) {
        for (TemplateRepository repository : templateRepositories) {
            Optional<Template> template = repository.resolve(templatePath);
            if (template.isPresent()) {
                return template;
            }
        }
        return Optional.empty();
    }

    @Override
    public File dir() {
        return dir;
    }

    public AssetsConfig assets() {
        return assetsConfig;
    }

    public TemplateEngine engine() {
        return templateEngine;
    }

    public TemplateDialect dialect() {
        return templateDialect;
    }

    @Override
    protected void configure() throws Exception {
        bind(TemplateConfig.class).to(this).export();

        add(new TemplateRepository(new FolderResourceRepository(dir)));

        templateDialect
            .add(new TemplateHrefAttrProcessor(dialect(), site.baseURL(), site.baseCdnURLs()))
            .add(new TemplateSrcAttrProcessor(dialect(), site.baseURL(), site.baseCdnURLs()))
            .add(new LangAttrProcessor(dialect()));


        if (site.isDebugEnabled()) {
            cacheManager.setTemplateCacheMaxSize(0);
            templateResolver.setCacheable(false);
        }

        templateEngine.addTemplateResolver(templateResolver);

        AssetsHandler assetsHandler = new AssetsHandler(assetsConfig)
            .enableMd5Path();

        route().get("/assets/*", assetsHandler)
            .get("/robots.txt", assetsHandler)
            .get("/favicon.ico", assetsHandler);

        error().on(BadRequestException.class, (request, e) -> {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            return Response.text(stackTrace.toString()).setStatusCode(401);
        });

        error().on(NotFoundException.class, (request, e) -> {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            return Response.text(stackTrace.toString()).setStatusCode(404);
        });

        error().on(Throwable.class, (request, e) -> {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            return Response.text(stackTrace.toString()).setStatusCode(500);
        });
    }


    void add(TemplateRepository resourceRepository) {
        templateRepositories.add(resourceRepository);
        assetsConfig.add(resourceRepository.raw());
    }

    @Override
    public List<Template> all() {
        List<Template> templates = Lists.newArrayList();
        templateRepositories.stream().forEach(templateRepository -> {
            templates.addAll(Lists.newArrayList(templateRepository)
                .stream()
                .filter(resource -> "html".equals(Files.getFileExtension(resource.path()))).collect(Collectors.toList()));
        });
        return templates;
    }
}
