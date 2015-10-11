package org.app4j.site.module.page.template.impl;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageIndexService;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.template.DirectoryObject;
import org.app4j.site.module.page.template.PageObject;
import org.app4j.site.runtime.database.FindView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageObjectImpl extends Page implements PageObject {
    protected final Page page;
    protected final PageService pageService;
    protected final PageIndexService pageIndexService;

    public PageObjectImpl(Page page, PageService pageService, PageIndexService pageIndexService) {
        this.page = page;
        this.pageService = pageService;
        this.pageIndexService = pageIndexService;

        putAll(page);
    }

    @Override
    public FindView<PageObject> relatedPages(int fetchSize) {
        FindView<Page> pages = pageIndexService.relatedPages(page, fetchSize);
        FindView<PageObject> results = new FindView<>(0, pages.total());
        results.addAll(pages.stream().map(page -> new PageObjectImpl(page, pageService, pageIndexService)).collect(Collectors.toList()));
        return results;
    }

    @Override
    public DirectoryObject directory() {
        List<String> tags = page.getTags();

        if (tags == null || tags.isEmpty()) {
            return new DirectoryObjectImpl(pageService.root(), pageService, pageIndexService, 0);
        }

        Page directory = pageService.findByKeyword(tags.get(0));
        if (directory == null) {
            return new DirectoryObjectImpl(pageService.root(), pageService, pageIndexService, 0);
        }

        return new DirectoryObjectImpl(directory, pageService, pageIndexService, 0);
    }

    @Override
    public List<DirectoryObject> tags() {
        List<String> tags = page.getTags();
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        List<DirectoryObject> directories = Lists.newArrayList();
        for (String tag : tags) {
            Page directory = pageService.findByKeyword(tag);

            if (directory != null) {
                directories.add(new DirectoryObjectImpl(directory, pageService, pageIndexService, 0));
            }
        }
        return directories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PageObjectImpl that = (PageObjectImpl) o;
        return Objects.equal(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), page);
    }
}
