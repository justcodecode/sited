package org.app4j.site.web.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author chi
 */
public class ByteArrayBodyResponseHandler implements BodyHandler {
    @Override
    public InputStream handle(ResponseImpl response) {
        ByteArrayBody body = (ByteArrayBody) response.body;
        return new ByteArrayInputStream(body.bytes);
    }
}
