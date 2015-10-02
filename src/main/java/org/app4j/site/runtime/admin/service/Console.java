package org.app4j.site.runtime.admin.service;

import org.app4j.site.Module;

/**
 * @author chi
 */
public class Console {
    private final Menu menu = new Menu();

    public Menu menu() {
        return menu;
    }

    public Console register(Module module) {
        return this;
    }
}
