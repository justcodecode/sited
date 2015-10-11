package org.app4j.site.module.page;

import org.app4j.site.module.page.domain.Page;

import java.util.Optional;

/**
 * @author chi
 */
public interface PageConfig {
    Iterable<Page> pages();

    Optional<Page> get(String path);


}
