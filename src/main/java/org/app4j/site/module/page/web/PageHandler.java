package org.app4j.site.module.page.web;

import org.app4j.site.module.page.variable.PagePath;
import org.app4j.site.module.page.variable.PageVariable;
import org.app4j.site.module.page.variable.VariableConfig;
import org.app4j.site.module.page.variable.VariableRef;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;
import java.util.Collections;

/**
 * @author chi
 */
public class    PageHandler implements Handler {
    protected final VariableConfig variableConfig;

    public PageHandler(VariableConfig variableConfig) {
        this.variableConfig = variableConfig;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response handle(Request request) throws IOException {
        PageContext pageContext = new PageContext();

        variableConfig.globalVariables().forEach(variableDef -> {
            VariableRef variableRef = new VariableRef(variableDef.name, variableDef.name, Collections.EMPTY_MAP);
            Object value = variableConfig.eval(variableRef, request);
            pageContext.put(variableRef.name, value);
        });

        if (pageContext.hasPage()) {
            PageVariable.Page page = (PageVariable.Page) pageContext.get("page");
            return Response.template(page.templatePath(), pageContext);
        } else {
            PagePath pagePath = new PagePath(request.path());
            return Response.template(pagePath.templatePath(), pageContext);
        }
    }
}
