package org.app4j.site.runtime.template;

import org.app4j.site.web.exception.NotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author chi
 */
public class AssetsConfig {
    private final Set<ResourceRepository> assetsRepositories = new TreeSet<>((o1, o2) -> o2.priority() - o1.priority());

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
