package org.app4j.site.web.impl;

import io.undertow.io.Sender;
import org.app4j.site.Site;
import org.thymeleaf.context.Context;

/**
 * @author neo
 */
public class TemplateBodyResponseHandler implements BodyHandler {
    private final Site site;

    public TemplateBodyResponseHandler(Site site) {
        this.site = site;
    }

    @Override
    public void handle(ResponseImpl response, Sender sender, RequestImpl request) {
        TemplateBody body = (TemplateBody) response.body;
        Context context = new Context();
        context.setVariables(body.model);
        String content = site.template().engine().process(body.templatePath, context);
        sender.send(content);
    }
}
