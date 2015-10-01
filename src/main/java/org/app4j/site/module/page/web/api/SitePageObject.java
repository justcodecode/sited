package org.app4j.site.module.page.web.api;

/**
 * @author chi
 */
public interface SitePageObject extends PageObject {
    DirectoryPageObject root();

    IndicesPageObject indices();

    TrackingPageObject tracking();
}
