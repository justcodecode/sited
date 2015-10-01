package org.app4j.site.web.impl;

import java.io.File;

/**
 * @author chi
 */
public class FileBody implements Body {
    final File file;

    public FileBody(File file) {
        this.file = file;
    }
}
