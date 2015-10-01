package org.app4j.site.web.impl;

import io.undertow.io.Sender;
import org.thymeleaf.TemplateEngine;

/**
 * @author neo
 */
public class TemplateBodyResponseHandler implements BodyHandler {
    private final TemplateEngine templateManager;

    public TemplateBodyResponseHandler(TemplateEngine templateManager) {
        this.templateManager = templateManager;
    }

    @Override
    public void handle(ResponseImpl response, Sender sender, RequestImpl request) {
        TemplateBody body = (TemplateBody) response.body;
        String content = templateManager.process(body.templatePath, null);
        sender.send(content);
    }
}
