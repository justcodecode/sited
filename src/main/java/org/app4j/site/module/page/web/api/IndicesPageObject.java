package org.app4j.site.module.page.web.api;

import org.app4j.site.runtime.database.FindView;

/**
 * @author chi
 */
public interface IndicesPageObject extends PageObject {
    FindView<PagePageObject> find(String keywords, int pageNumber);
}
