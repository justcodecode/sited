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
import org.app4j.site.web.exception.NotFoundException;
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

/**
 * @author chi
 */
public class TemplateConfig extends InternalModule {
    private final TemplateDialect templateDialect = new TemplateDialect();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private final StandardCacheManager cacheManager = new StandardCacheManager();
    private final List<ResourceRepository> templateRepositories = Lists.newArrayList();
    private final AssetsConfig assetsConfig = new AssetsConfig();

    public TemplateConfig() {
        templateEngine.setCacheManager(cacheManager);
        templateEngine.setDialect(templateDialect);
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteModule.class);
    }

    public TemplateConfig add(ResourceRepository resourceRepository) {
        templateRepositories.add(resourceRepository);
        return this;
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

        if (site.isDebugEnabled()) {
            cacheManager.setTemplateCacheMaxSize(0);
        }

        TemplateResolver templateResolver = new TemplateResolver();
        templateResolver.setCharacterEncoding(Charsets.UTF_8.name());
        templateResolver.setResourceResolver(new IResourceResolver() {
            @Override
            public String getName() {
                return "sited";
            }

            @Override
            public IResource resolveResource(IEngineConfiguration configuration, IContext context, String resource, String characterEncoding) {
                Resource template = get(resource);
                return new StringResource(template.path(), new String(template.content(), Charsets.UTF_8));
            }
        });

        templateDialect
                .add(new TemplateHrefAttrProcessor(dialect(), site().baseURL(), site().baseCdnURLs()))
                .add(new TemplateSrcAttrProcessor(dialect(), site().baseURL(), site().baseCdnURLs()))
                .add(new LangAttrProcessor(dialect()));


        if (site.isDebugEnabled()) {
            templateResolver.setCacheable(false);
        }
        templateEngine.addTemplateResolver(templateResolver);

        bind(TemplateConfig.class).to(this).export();
    }

    public Resource get(String path) {
        for (ResourceRepository templateLoader : templateRepositories) {
            Optional<Resource> resourceOptional = templateLoader.load(path);
            if (resourceOptional.isPresent()) {
                return resourceOptional.get();
            }
        }
        throw new NotFoundException(path);
    }

    public List<Resource> all() {
        List<Resource> templates = Lists.newArrayList();
        for (ResourceRepository templateRepository : templateRepositories) {
            if (templateRepository instanceof FolderResourceRepository) {
                List<Resource> resources = Lists.newArrayList(templateRepository);
                for (Resource resource : resources) {
                    if ("html".equals(Files.getFileExtension(resource.path()))) {
                        templates.add(resource);
                    }
                }
            }
        }
        return templates;
    }
}
