package org.app4j.site.internal.admin.service;

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
