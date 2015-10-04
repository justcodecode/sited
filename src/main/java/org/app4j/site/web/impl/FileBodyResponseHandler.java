package org.app4j.site.web.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author chi
 */
public class FileBodyResponseHandler implements BodyHandler {
    @Override
    public InputStream handle(ResponseImpl response) {
        File file = ((FileBody) response.body).file;

        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
