package org.app4j.site.runtime.template;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class ClasspathResourceRepository implements ResourceRepository {
    private final String prefix;
    private final int priority;

    public ClasspathResourceRepository(String prefix, int priority) {
        this.priority = priority;
        this.prefix = prefix.endsWith("/") ? prefix.substring(0, prefix.length() - 1) : prefix;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public Optional<Resource> load(String path) {
        Preconditions.checkState(path.startsWith("/"), "path %s must start with /", path);
        try (InputStream inputStream = ClasspathResourceRepository.class.getClassLoader().getResourceAsStream(prefix + path)) {
            if (inputStream == null) {
                return Optional.empty();
            }

            return Optional.of(new Resource(path, ByteStreams.toByteArray(inputStream)));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Override
    public Iterator<Resource> iterator() {
        throw new Error("not implemented");
    }
}
