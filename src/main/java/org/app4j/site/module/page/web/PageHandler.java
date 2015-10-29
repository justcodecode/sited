package org.app4j.site.module.page.web;

import org.app4j.site.internal.template.Template;
import org.app4j.site.internal.template.TemplateConfig;
import org.app4j.site.module.page.variable.PagePath;
import org.app4j.site.module.page.variable.PageVariable;
import org.app4j.site.module.page.variable.VariableConfig;
import org.app4j.site.module.page.variable.VariableRef;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author chi
 */
public class PageHandler implements Handler {
    protected final VariableConfig variableConfig;
    private final TemplateConfig templateConfig;

    public PageHandler(VariableConfig variableConfig, TemplateConfig templateConfig) {
        this.variableConfig = variableConfig;
        this.templateConfig = templateConfig;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response handle(Request request) throws IOException {
        PageContext pageContext = new PageContext();
        pageContext.put("__request__", request);

        variableConfig.globalVariables().forEach(variableDef -> {
            VariableRef variableRef = new VariableRef(variableDef.name, variableDef.name, Collections.EMPTY_MAP);
            Object value = variableConfig.eval(variableRef, request);
            pageContext.put(variableRef.name, value);
        });

        if (pageContext.hasPage()) {
            PageVariable.Page page = (PageVariable.Page) pageContext.get("page");

            Optional<Template> template = templateConfig.get(page.templatePath());
            if (!template.isPresent()) {
                throw new NotFoundException(request.path());
            }
            pageContext.put("__template__", template.get());
            return Response.template(page.templatePath(), pageContext);
        } else {
            PagePath pagePath = new PagePath(request.path());

            Optional<Template> template = templateConfig.get(pagePath.templatePath());
            if (!template.isPresent()) {
                throw new NotFoundException(request.path());
            }
            pageContext.put("__template__", template.get());
            return Response.template(pagePath.templatePath(), pageContext);
        }
    }
}
