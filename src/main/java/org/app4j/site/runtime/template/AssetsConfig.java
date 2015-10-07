package org.app4j.site.runtime.template;

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

    public Optional<Resource> get(String path) {
        for (ResourceRepository resourceRepository : assetsRepositories) {
            Optional<Resource> resourceOptional = resourceRepository.load(path);
            if (resourceOptional.isPresent()) {
                return Optional.of(resourceOptional.get());
            }
        }
        return Optional.empty();
    }
}
