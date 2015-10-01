package org.app4j.site.runtime.template.admin.web;

import org.app4j.site.runtime.template.TemplateConfig;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class TemplateRESTController {
    private final TemplateConfig templateConfig;

    public TemplateRESTController(TemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
    }

    public Response all(Request request) throws IOException {
        return Response.bean(templateConfig.all());
    }
}
