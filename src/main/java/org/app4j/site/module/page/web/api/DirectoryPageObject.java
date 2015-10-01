package org.app4j.site.module.page.web.api;

import org.app4j.site.runtime.database.FindView;

/**
 * @author chi
 */
public interface DirectoryPageObject extends PagePageObject {
    FindView<PagePageObject> pages(int offset, int fetchSize);

    FindView<DirectoryPageObject> directories(int offset, int fetchSize);

    int pageNumber();

    long totalPages();
}
