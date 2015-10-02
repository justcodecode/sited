package org.app4j.site.runtime.admin.service;

/**
 * @author chi
 */
public class Console {
    private final Menu menu = new Menu();

    public Menu menu() {
        return menu;
    }

    public Console add(String module) {
        return this;
    }
}
