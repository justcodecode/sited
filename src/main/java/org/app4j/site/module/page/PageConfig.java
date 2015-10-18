package org.app4j.site.module.page;

import org.app4j.site.module.page.variable.VariableConfig;

import java.util.Optional;

/**
 * @author chi
 */
public interface PageConfig {
    Optional<Page> get(String path);

    VariableConfig variables();
}
