package org.app4j.site.module.page.variable;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.runtime.database.FindView;

import java.util.Collections;
import java.util.List;

/**
 * @author chi
 */
public class PageObjectImpl extends Page implements PageObject {
    protected final Page page;
    protected final PageService pageService;

    public PageObjectImpl(Page page, PageService pageService) {
        this.page = page;
        this.pageService = pageService;
        putAll(page);
    }

    @Override
    public FindView<PageObject> relatedPages(int offset, int fetchSize) {
//        FindView<Page> pages = pageService.index().search(page.getTitle(), offset, fetchSize);
//        FindView<PageObject> results = new FindView<>(offset, pages.total());
//        results.addAll(pages.stream().map(page -> new PageObjectImpl(page, pageService)).collect(Collectors.toList()));
//        return results;
        return null;
    }

    @Override
    public DirectoryObject directory() {
        List<String> tags = page.getTags();

        if (tags == null || tags.isEmpty()) {
            return new DirectoryObjectImpl(pageService.root(), pageService, 0);
        }

        Page directory = pageService.findByKeyword(tags.get(0));
        if (directory == null) {
            return new DirectoryObjectImpl(pageService.root(), pageService, 0);
        }

        return new DirectoryObjectImpl(directory, pageService, 0);
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
                directories.add(new DirectoryObjectImpl(directory, pageService, 0));
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
