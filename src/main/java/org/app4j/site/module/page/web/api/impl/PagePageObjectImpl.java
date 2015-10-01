package org.app4j.site.module.page.web.api.impl;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.web.api.DirectoryPageObject;
import org.app4j.site.module.page.web.api.PagePageObject;
import org.app4j.site.runtime.database.FindView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PagePageObjectImpl extends Page implements PagePageObject {
    protected final Page page;
    protected final transient PageService pageService;

    public PagePageObjectImpl(Page page, PageService pageService) {
        this.page = page;
        this.pageService = pageService;
        putAll(page);
    }

    @Override
    public FindView<PagePageObject> relatedPages(int offset, int fetchSize) {
        FindView<Page> pages = pageService.indexService().findRelatedPages(page, offset, fetchSize);
        FindView<PagePageObject> results = new FindView<>(offset, pages.total());
        results.addAll(pages.stream().map(page -> new PagePageObjectImpl(page, pageService)).collect(Collectors.toList()));
        return results;
    }

    @Override
    public DirectoryPageObject directory() {
        List<String> tags = page.getTags();

        if (tags == null || tags.isEmpty()) {
            return new DirectoryPageObjectImpl(pageService.root(), pageService, 0);
        }

        Page directory = pageService.findByKeyword(tags.get(0));
        if (directory == null) {
            return new DirectoryPageObjectImpl(pageService.root(), pageService, 0);
        }

        return new DirectoryPageObjectImpl(directory, pageService, 0);
    }

    @Override
    public List<DirectoryPageObject> tags() {
        List<String> tags = page.getTags();
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        List<DirectoryPageObject> directories = Lists.newArrayList();
        for (String tag : tags) {
            Page directory = pageService.findByKeyword(tag);

            if (directory != null) {
                directories.add(new DirectoryPageObjectImpl(directory, pageService, 0));
            }
        }
        return directories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PagePageObjectImpl that = (PagePageObjectImpl) o;
        return Objects.equal(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), page);
    }
}
