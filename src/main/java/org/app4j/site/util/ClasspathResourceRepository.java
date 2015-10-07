package org.app4j.site.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author chi
 */
public class ClasspathResourceRepository implements ResourceRepository {
    private final String classpath;

    public ClasspathResourceRepository(String classpath) {
        Preconditions.checkState(classpath.startsWith("/"), "classpath must start with /");
        this.classpath = classpath.endsWith("/")
                ? classpath.substring(0, classpath.length() - 1)
                : classpath;
    }

    @Override
    public Optional<Resource> resolve(String path) {
        Preconditions.checkState(path.startsWith("/"), "path must start with /");
        InputStream inputStream = getClass().getResourceAsStream(classpath + path);
        if (inputStream == null) {
            return Optional.empty();
        }
        return Optional.of(new Resource(path, () -> inputStream));
    }

    @Override
    public Iterator<Resource> iterator() {
        return Lists.<Resource>newArrayList().iterator();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("classpath", classpath)
                .toString();
    }
}
