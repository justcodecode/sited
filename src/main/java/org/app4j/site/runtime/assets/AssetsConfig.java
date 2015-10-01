package org.app4j.site.runtime.assets;

import com.google.common.collect.Lists;
import org.app4j.site.Module;
import org.app4j.site.runtime.InternalModule;
import org.app4j.site.runtime.assets.web.AssetsHandler;
import org.app4j.site.runtime.route.NotFoundException;
import org.app4j.site.runtime.route.RouteConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class AssetsConfig extends InternalModule {
    private final List<ResourceRepository> assetsRepositories = Lists.newArrayList();

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(RouteConfig.class);
    }

    @Override
    protected void configure() throws Exception {
        route().get("/assets/*", new AssetsHandler(this));

        bind(AssetsConfig.class).to(this).export();
    }

    @Override
    protected String name() {
        return "assets";
    }

    public AssetsConfig addResourceRepository(ResourceRepository resourceRepository) {
        assetsRepositories.add(resourceRepository);
        return this;
    }

    public Resource get(String path) {
        for (ResourceRepository resourceRepository : assetsRepositories) {
            Optional<Resource> resourceOptional = resourceRepository.load(path);
            if (resourceOptional.isPresent()) {
                return resourceOptional.get();
            }
        }
        throw new NotFoundException(path);
    }
}
