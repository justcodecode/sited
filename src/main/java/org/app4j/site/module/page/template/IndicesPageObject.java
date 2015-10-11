package org.app4j.site.module.page.template;

import org.app4j.site.runtime.database.FindView;

/**
 * @author chi
 */
public interface IndicesPageObject {
    FindView<PageObject> find(String keywords, int pageNumber);
}
