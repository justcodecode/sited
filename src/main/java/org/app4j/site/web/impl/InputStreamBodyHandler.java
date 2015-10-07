package org.app4j.site.web.impl;

import java.io.InputStream;

/**
 * @author chi
 */
public class InputStreamBodyHandler implements BodyHandler {
    @Override
    public InputStream handle(ResponseImpl response) {
        InputStreamBody body = (InputStreamBody) response.body;
        return body.inputStream;
    }
}
