package org.app4j.site.util;

import java.util.Optional;

/**
 * @author chi
 */
public interface ResourceRepository extends Iterable<Resource> {
    Optional<Resource> resolve(String path);
}
