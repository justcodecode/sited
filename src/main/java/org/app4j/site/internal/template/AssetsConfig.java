package org.app4j.site.internal.template;

import org.app4j.site.util.Resource;
import org.app4j.site.util.ResourceRepository;
import org.app4j.site.util.SortedList;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class AssetsConfig {
    private final List<ResourceRepository> assetsRepositories = new SortedList<>((o1, o2) -> o2.hashCode() - o1.hashCode());

    public AssetsConfig add(ResourceRepository resourceRepository) {
        assetsRepositories.add(resourceRepository);
        return this;
    }


    public Optional<Resource> get(String path) {
        for (ResourceRepository resourceRepository : assetsRepositories) {
            Optional<Resource> resourceOptional = resourceRepository.resolve(path);
            if (resourceOptional.isPresent()) {
                return Optional.of(resourceOptional.get());
            }
        }
        return Optional.empty();
    }
}
