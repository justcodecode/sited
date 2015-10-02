package org.app4j.site.runtime.template;

import com.google.common.collect.Lists;
import org.app4j.site.web.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class AssetsConfig {
    private final List<ResourceRepository> assetsRepositories = Lists.newArrayList();

    public AssetsConfig add(ResourceRepository resourceRepository) {
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
