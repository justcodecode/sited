package org.app4j.site.runtime.admin.service;

import com.google.common.collect.Lists;
import org.app4j.site.runtime.template.Resource;
import org.app4j.site.runtime.template.ResourceRepository;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class AdminResourceRepository implements ResourceRepository {
    private final ResourceRepository resourceRepository;

    public AdminResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public Optional<Resource> load(String path) {
        return resourceRepository.load(path);
    }

    @Override
    public Iterator<Resource> iterator() {
        return Lists.<Resource>newArrayList().iterator();
    }
}
