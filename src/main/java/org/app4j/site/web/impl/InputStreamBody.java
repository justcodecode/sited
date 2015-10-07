package org.app4j.site.web.impl;

import java.io.InputStream;

/**
 * @author chi
 */
public class InputStreamBody implements Body {
    final InputStream inputStream;

    public InputStreamBody(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
