package org.app4j.site.module.page.web.api;

import org.app4j.site.module.page.variable.PageObject;
import org.app4j.site.runtime.database.FindView;

/**
 * @author chi
 */
public interface IndicesPageObject {
    FindView<PageObject> find(String keywords, int pageNumber);
}
