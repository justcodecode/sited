package org.app4j.site.module.page.web.api;

import org.app4j.site.runtime.database.FindView;

import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public interface TrackingPageObject extends PageObject {
    FindView<PagePageObject> topReadPages(TimeUnit timeUnit, int offset, int fetchSize);

    FindView<PagePageObject> topLikedPages(TimeUnit timeUnit, int offset, int fetchSize);
}
