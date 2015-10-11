package org.app4j.site.module.page.template;

/**
 * @author chi
 */
public interface RequestPageObject {
    String path();

    String param(String name);

    String cookie(String cookie);

    String header(String header);

    String url();

    String host();
}
