package org.app4j.site.runtime.assets;

import java.util.Optional;

/**
 * @author chi
 */
public interface ResourceRepository extends Iterable<Resource> {
    Optional<Resource> load(String path);
}
