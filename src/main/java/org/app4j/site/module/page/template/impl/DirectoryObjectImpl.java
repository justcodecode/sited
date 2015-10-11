package org.app4j.site.module.page.template.impl;

import com.google.common.base.Objects;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.template.DirectoryObject;
import org.app4j.site.module.page.template.PageObject;
import org.app4j.site.runtime.database.FindView;

import java.util.stream.Collectors;

/**
 * @author chi
 */
public class DirectoryObjectImpl extends PageObjectImpl implements DirectoryObject {
    private final int pageNumber;

    public DirectoryObjectImpl(Page page, PageService pageService, int pageNumber) {
        super(page, pageService);
        this.pageNumber = pageNumber;
    }

    @Override
    public FindView<PageObject> pages(int offset, int fetchSize) {
        FindView<Page> pages = pageService.find(offset, fetchSize);
        FindView<PageObject> results = new FindView<>(offset, pages.total());
        results.addAll(pages.stream().map(page1 -> new PageObjectImpl(page1, pageService)).collect(Collectors.toList()));
        return results;
    }

    @Override
    public FindView<DirectoryObject> directories(int offset, int fetchSize) {
        FindView<Page> pages = pageService.findByCategory(page.getPath(), offset, fetchSize, "directory");
        FindView<DirectoryObject> results = new FindView<>(offset, pages.total());
        results.addAll(pages.stream().map(page1 -> new DirectoryObjectImpl(page1, pageService, 0)).collect(Collectors.toList()));
        return results;
    }

    @Override
    public int pageNumber() {
        return pageNumber;
    }

    @Override
    public long totalPages() {
        return pageService.countByCategory(page.getPath());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DirectoryObjectImpl that = (DirectoryObjectImpl) o;
        return Objects.equal(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), page);
    }
}
