package org.app4j.site.web.impl;

import com.google.common.base.Charsets;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author neo
 */
public class TextBodyResponseHandler implements BodyHandler {
    @Override
    public InputStream handle(ResponseImpl response) {
        TextBody body = (TextBody) response.body;
        return new ByteArrayInputStream(body.text.getBytes(Charsets.UTF_8));
    }
}
