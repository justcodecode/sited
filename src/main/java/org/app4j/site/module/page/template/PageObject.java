package org.app4j.site.module.page.template;

import org.app4j.site.runtime.database.FindView;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public interface PageObject extends Map<String, Object> {
    FindView<PageObject> relatedPages(int fetchSize);

    DirectoryObject directory();

    List<DirectoryObject> tags();
}
