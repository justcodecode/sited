package org.app4j.site.runtime.template;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private final int priority;

    public FolderResourceRepository(File dir, int priority) {
        this.dir = dir;
        this.priority = priority;
    }

    @Override
    public Optional<Resource> load(String path) {
        File file = new File(dir, path);
        if (file.exists() && file.isFile()) {
            return Optional.of(new Resource(path, () -> {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new Error(e);
                }
            }));
        }
        return Optional.empty();
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public Iterator<Resource> iterator() {
        List<Resource> all = Lists.newArrayList();
        try {
            java.nio.file.Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Resource resource = new Resource(dir.toPath().relativize(file).toString(), null);
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
