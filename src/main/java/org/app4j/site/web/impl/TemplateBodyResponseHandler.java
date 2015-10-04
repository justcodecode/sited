package org.app4j.site.web.impl;

import com.google.common.base.Charsets;
import org.app4j.site.Site;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author chi
 */
public class TemplateBodyResponseHandler implements BodyHandler {
    private final Site site;

    public TemplateBodyResponseHandler(Site site) {
        this.site = site;
    }

    @Override
    public InputStream handle(ResponseImpl response) {
        TemplateBody body = (TemplateBody) response.body;
        ResponseImpl templateResponse = (ResponseImpl) site.render(body.templatePath, body.model);
        response.setContentType(templateResponse.contentType);
        response.setStatusCode(templateResponse.statusCode);

        TextBody textBody = (TextBody) templateResponse.body;
        return new ByteArrayInputStream(textBody.text.getBytes(Charsets.UTF_8));
    }
}
