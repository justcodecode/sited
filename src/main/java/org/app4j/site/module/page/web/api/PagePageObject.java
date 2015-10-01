package org.app4j.site.module.page.web.api;

import org.app4j.site.runtime.database.FindView;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public interface PagePageObject extends Map<String, Object>, PageObject {
    FindView<PagePageObject> relatedPages(int offset, int fetchSize);

    DirectoryPageObject directory();

    List<DirectoryPageObject> tags();
}
