package org.app4j.site.module.page.variable;

import com.google.common.collect.Lists;
import org.app4j.site.Scope;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.runtime.database.FindView;
import org.app4j.site.web.Request;
import org.bson.Document;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class PageVariable implements Variable<PageVariable.Page> {
    private final PageService pageService;

    public PageVariable(PageService pageService) {
        this.pageService = pageService;
    }

    @Override
    public PageVariable.Page eval(VariableRef variableRef, Scope scope) {
        Request request = scope.require(Request.class);
        PagePath pagePath = new PagePath(request.path());

        Optional<org.app4j.site.module.page.Page> page = pageService.findByPath(pagePath.pagePath());
        if (page.isPresent()) {
            if (pagePath.isDirectory()) {
                return new Page(page.get(), pageService);
            } else {
                return new Directory(page.get(), pageService, 0, 0);
            }
        } else {
            return null;
        }
    }

    public static class Page extends Document {
        protected final org.app4j.site.module.page.Page page;
        protected final PageService pageService;

        public Page(org.app4j.site.module.page.Page page, PageService pageService) {
            this.page = page;
            this.pageService = pageService;

            putAll(page);
        }

        public String templatePath() {
            return page.templatePath();
        }

        public FindView<Page> relatedPages(int fetchSize) {
            return new FindView<>(0, 0);
        }

        public Directory directory() {
            return null;
        }

        public List<Directory> tags() {
            if (page.tags() == null) {
                return Collections.emptyList();
            }
            List<Directory> tags = Lists.newArrayList();
            for (String tag : page.tags()) {
                org.app4j.site.module.page.Page page = pageService.findByKeyword(tag);
                if (page != null) {
                    tags.add(new Directory(page, pageService, 0, 0));
                }
            }
            return tags;
        }
    }

    public static class Directory extends Page {
        public final int offset;
        public final int fetchSize;
        public final int total;

        public Directory(org.app4j.site.module.page.Page page, PageService pageService, int offset, int fetchSize) {
            super(page, pageService);

            this.offset = offset;
            this.fetchSize = fetchSize;
            total = 0;
        }

        public FindView<Page> pages() {
            return null;
        }

        public FindView<Directory> subDirectories() {
            return null;
        }
    }
}
