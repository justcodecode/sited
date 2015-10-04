package org.app4j.site.web.impl;

import java.util.Map;

/**
 * @author chi
 */
public class TemplateBody implements Body {
    final String templatePath;
    final Map<String, Object> model;

    public TemplateBody(String templatePath, Map<String, Object> model) {
        this.templatePath = templatePath;
        this.model = model;
    }
}
