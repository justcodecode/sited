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
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.StandardCacheManager;
import org.thymeleaf.context.IContext;
import org.thymeleaf.resource.IResource;
import org.thymeleaf.resource.StringResource;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author chi
 */
public class TemplateModule extends InternalModule implements TemplateConfig {
    private final TemplateDialect templateDialect = new TemplateDialect();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private final StandardCacheManager cacheManager = new StandardCacheManager();
    private final Set<ResourceRepository> templateRepositories = new TreeSet<>((o1, o2) -> o2.priority() - o1.priority());
    private final AssetsConfig assetsConfig = new AssetsConfig();

    public TemplateModule() {
        templateEngine.setCacheManager(cacheManager);
        templateEngine.setDialect(templateDialect);
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteModule.class);
    }

    public TemplateModule add(ResourceRepository resourceRepository) {
        templateRepositories.add(resourceRepository);
        return this;
    }

    @Override
    public Optional<Resource> get(String templatePath) {
        for (ResourceRepository templateLoader : templateRepositories) {
            Optional<Resource> resourceOptional = templateLoader.load(templatePath);
            if (resourceOptional.isPresent()) {
                return Optional.of(resourceOptional.get());
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
        Site site = site();

        TemplateResolver templateResolver = new TemplateResolver();
        templateResolver.setCharacterEncoding(Charsets.UTF_8.name());
        templateResolver.setResourceResolver(new IResourceResolver() {
            @Override
            public String getName() {
                return "template";
            }

            @Override
            public IResource resolveResource(IEngineConfiguration configuration, IContext context, String resource, String characterEncoding) {
                Resource template = get(resource).get();
                return new StringResource(template.path(), new String(template.content(), Charsets.UTF_8));
            }
        });

        templateDialect
                .add(new TemplateHrefAttrProcessor(dialect(), site().baseURL(), site().baseCdnURLs()))
                .add(new TemplateSrcAttrProcessor(dialect(), site().baseURL(), site().baseCdnURLs()))
                .add(new LangAttrProcessor(dialect()));


        if (site.isDebugEnabled()) {
            cacheManager.setTemplateCacheMaxSize(0);
            templateResolver.setCacheable(false);
        }

        templateEngine.addTemplateResolver(templateResolver);
        bind(TemplateConfig.class).to(this).export();
    }

    public List<Resource> all() {
        List<Resource> templates = Lists.newArrayList();
        templateRepositories.stream().filter(templateRepository -> templateRepository instanceof FolderResourceRepository).forEach(templateRepository -> {
            List<Resource> resources = Lists.newArrayList(templateRepository);
            for (Resource resource : resources) {
                if ("html".equals(Files.getFileExtension(resource.path()))) {
                    templates.add(resource);
                }
            }
        });
        return templates;
    }
}
