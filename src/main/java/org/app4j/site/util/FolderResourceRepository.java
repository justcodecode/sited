package org.app4j.site.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
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
    public Iterator<Resource> iterator() {
        Iterator<File> iterator = Files.iterate(dir);
        return new Iterator<Resource>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Resource next() {
                File file = iterator.next();
                return new Resource("/" + dir.toPath().relativize(file.toPath()).toString(), () -> {
                    try {
                        return new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        throw new Error(e);
                    }
                });
            }
        };
    }

    @Override
    public Optional<Resource> resolve(String path) {
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
}
