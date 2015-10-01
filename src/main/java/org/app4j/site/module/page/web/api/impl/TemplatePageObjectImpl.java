package org.app4j.site.module.page.web.api.impl;

import org.app4j.site.module.page.web.api.TemplatePageObject;
import org.app4j.site.runtime.assets.Resource;

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
