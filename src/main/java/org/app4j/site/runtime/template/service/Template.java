package org.app4j.site.runtime.template.service;

import org.app4j.site.util.Resource;

/**
 * @author chi
 */
public class Template {
    private final Resource resource;

    public Template(Resource resource) {
        this.resource = resource;
    }

    public String path() {
        return resource.path();
    }

    public String text() {
        return resource.text();
    }
}
