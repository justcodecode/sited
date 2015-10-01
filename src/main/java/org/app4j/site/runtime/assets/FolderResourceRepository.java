package org.app4j.site.runtime.assets;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class FolderResourceRepository implements ResourceRepository {
    private final File dir;

    public FolderResourceRepository(File dir) {
        this.dir = dir;
    }

    @Override
    public Optional<Resource> load(String path) {
        File file = new File(dir, path);
        if (file.exists() && file.isFile()) {
            try {
                return Optional.of(new Resource(path, Files.toByteArray(file)));
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Iterator<Resource> iterator() {
        List<Resource> all = Lists.newArrayList();
        try {
            java.nio.file.Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Resource resource = new Resource(dir.toPath().resolve(file).toString(), null);
                    all.add(resource);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new Error(e);
        }
        return all.iterator();
    }
}
