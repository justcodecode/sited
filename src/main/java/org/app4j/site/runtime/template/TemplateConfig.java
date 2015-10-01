package org.app4j.site.runtime.template;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.assets.AssetsConfig;
import org.app4j.site.runtime.assets.FolderResourceRepository;
import org.app4j.site.runtime.assets.Resource;
import org.app4j.site.runtime.assets.ResourceRepository;
import org.app4j.site.runtime.route.NotFoundException;
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

import java.io.File;
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

    public TemplateConfig() {
        templateEngine.setCacheManager(cacheManager);
        templateEngine.setDialect(templateDialect);
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(AssetsConfig.class);
    }

    public TemplateConfig addResourceRepository(ResourceRepository resourceRepository) {
        templateRepositories.add(resourceRepository);
        return this;
    }


    public TemplateEngine engine() {
        return templateEngine;
    }


    public TemplateDialect dialect() {
        return templateDialect;
    }

    @Override
    protected void configure() throws Exception {
        Site site = require(Site.class);

        if (site.isDebugEnabled()) {
            cacheManager.setTemplateCacheMaxSize(0);
        }

        if (property("site.template.dir").isPresent()) {
            File templateDir = new File(property("site.template.dir").get());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
            }
            FolderResourceRepository resourceRepository = new FolderResourceRepository(templateDir);
            addResourceRepository(resourceRepository);
            assets().addResourceRepository(resourceRepository);
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

    @Override
    protected String name() {
        return "template";
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
