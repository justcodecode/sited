package org.app4j.site.module.page.template;

import org.app4j.site.runtime.database.FindView;

import java.util.concurrent.TimeUnit;

/**
 * @author chi
 */
public interface TrackingPageObject {
    FindView<PageObject> topReadPages(TimeUnit timeUnit, int offset, int fetchSize);

    FindView<PageObject> topLikedPages(TimeUnit timeUnit, int offset, int fetchSize);
}
