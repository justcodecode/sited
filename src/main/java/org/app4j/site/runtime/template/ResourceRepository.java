package org.app4j.site.runtime.template;

import java.util.Optional;

/**
 * @author chi
 */
public interface ResourceRepository extends Iterable<Resource> {
    Optional<Resource> load(String path);
}
