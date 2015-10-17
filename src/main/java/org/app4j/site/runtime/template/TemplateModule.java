package org.app4j.site.runtime.template;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.route.RouteModule;
import org.app4j.site.runtime.template.processor.LangAttrProcessor;
import org.app4j.site.runtime.template.processor.TemplateHrefAttrProcessor;
import org.app4j.site.runtime.template.processor.TemplateSrcAttrProcessor;
import org.app4j.site.runtime.template.service.Template;
import org.app4j.site.runtime.template.service.TemplateDialect;
import org.app4j.site.runtime.template.service.TemplateRepository;
import org.app4j.site.runtime.template.web.AssetsHandler;
import org.app4j.site.util.FolderResourceRepository;
import org.app4j.site.util.ResourceRepository;
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
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class TemplateModule extends InternalModule implements TemplateConfig {
    private final TemplateDialect templateDialect = new TemplateDialect();
    private final TemplateResolver templateResolver = new TemplateResolver();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private final StandardCacheManager cacheManager = new StandardCacheManager();
    private final Set<TemplateRepository> templateRepositories = new TreeSet<>((o1, o2) -> o2.priority() - o1.priority());
    private final File dir;

    private AssetsConfig assetsConfig;

    public TemplateModule(Site site) {
        super(site);
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
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteModule.class);
    }

    public TemplateModule add(ResourceRepository resourceRepository) {
        templateRepositories.add(new TemplateRepository(resourceRepository));
        assetsConfig.add(resourceRepository);
        return this;
    }

    @Override
    public TemplateConfig add(TemplateRepository resourceRepository) {
        templateRepositories.add(resourceRepository);
        assetsConfig.add(resourceRepository.raw());
        return this;
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

        assetsConfig = new AssetsConfig(cache().createDiskCache("assets", Integer.MAX_VALUE, TimeUnit.DAYS));
        add(new TemplateRepository(new FolderResourceRepository(dir)));

        templateDialect
            .add(new TemplateHrefAttrProcessor(dialect(), site().baseURL(), site().baseCdnURLs()))
            .add(new TemplateSrcAttrProcessor(dialect(), site().baseURL(), site().baseCdnURLs()))
            .add(new LangAttrProcessor(dialect()));


        if (site().isDebugEnabled()) {
            cacheManager.setTemplateCacheMaxSize(0);
            templateResolver.setCacheable(false);
        }

        templateEngine.addTemplateResolver(templateResolver);

        AssetsHandler assetsHandler = new AssetsHandler(assetsConfig)
            .enableMd5Path();

        if (!site().isDebugEnabled()) {
            assetsHandler.enableMinifyJS().enableMinifyCSS();
        }

        route().get("/assets/*", assetsHandler)
            .get("/robots.txt", assetsHandler)
            .get("/favicon.ico", assetsHandler);

        error().on(BadRequestException.class, (request, e) -> {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            return Response.text(stackTrace.toString(), "text/html").setStatusCode(401);
        });

        error().on(NotFoundException.class, (request, e) -> {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            return Response.text(stackTrace.toString(), "text/html").setStatusCode(404);
        });

        error().on(Throwable.class, (request, e) -> {
            StringWriter stackTrace = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTrace));
            return Response.text(stackTrace.toString(), "text/html").setStatusCode(500);
        });
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
