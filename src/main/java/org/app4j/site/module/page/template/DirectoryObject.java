package org.app4j.site.module.page.template;

import org.app4j.site.runtime.database.FindView;

/**
 * @author chi
 */
public interface DirectoryObject {
    FindView<PageObject> pages(int offset, int fetchSize);

    FindView<DirectoryObject> directories(int offset, int fetchSize);

    int pageNumber();

    long totalPages();
}
