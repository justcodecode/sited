package org.app4j.site.module.file.service;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chi
 */
public class FolderFileRepository implements FileRepository {
    private final File dir;

    public FolderFileRepository(File dir) {
        this.dir = dir;
    }

    @Override
    public InputStream get(String path) throws FileNotFoundException {
        File file = new File(dir, path);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        return null;
    }

    @Override
    public String put(InputStream inputStream, String fileExtension) {
        try {
            byte[] content = ByteStreams.toByteArray(inputStream);
            String path = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + '/' + Hashing.md5().hashBytes(content).toString() + '.' + fileExtension;
            File file = new File(dir, path);
            com.google.common.io.Files.createParentDirs(file);
            com.google.common.io.Files.write(content, file);
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
