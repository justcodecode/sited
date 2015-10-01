package org.app4j.site.module.file.service;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author chi
 */
public interface FileRepository {
    InputStream get(String path) throws FileNotFoundException;

    String put(InputStream inputStream, String fileExtension);
}
