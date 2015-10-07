package org.app4j.site.runtime.template.service;

import org.app4j.site.util.ClasspathResourceRepository;
import org.app4j.site.util.Resource;
import org.app4j.site.util.ResourceRepository;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class TemplateRepository implements Iterable<Template> {
    private final int priority;
    private final ResourceRepository resourceRepository;

    public TemplateRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
        this.priority = resourceRepository instanceof ClasspathResourceRepository
                ? 10 : 100;
    }

    public Optional<Template> resolve(String path) {
        Optional<Resource> resource = resourceRepository.resolve(path);
        if (resource.isPresent()) {
            return Optional.of(new Template(resource.get()));
        }
        return Optional.empty();
    }

    public int priority() {
        return priority;
    }

    public ResourceRepository raw() {
        return resourceRepository;
    }

    @Override
    public Iterator<Template> iterator() {
        Iterator<Resource> iterator = resourceRepository.iterator();
        return new Iterator<Template>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Template next() {
                return new Template(iterator.next());
            }
        };
    }
}
