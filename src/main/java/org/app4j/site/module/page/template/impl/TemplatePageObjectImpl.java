package org.app4j.site.module.page.template.impl;

import org.app4j.site.module.page.template.TemplatePageObject;
import org.app4j.site.util.Resource;

/**
 * @author chi
 */
public class TemplatePageObjectImpl implements TemplatePageObject {
    private final Resource template;

    public TemplatePageObjectImpl(Resource template) {
        this.template = template;
    }

    @Override
    public String path() {
        return template.path();
    }
}
