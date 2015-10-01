package org.app4j.site.module.page.web;

import com.google.common.collect.Maps;
import org.app4j.site.Site;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.page.web.api.impl.DirectoryPageObjectImpl;
import org.app4j.site.module.page.web.api.impl.PagePageObjectImpl;
import org.app4j.site.module.page.web.api.impl.RequestPageObjectImpl;
import org.app4j.site.module.page.web.api.impl.SitePageObjectImpl;
import org.app4j.site.runtime.template.Resource;
import org.app4j.site.runtime.route.NotFoundException;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class PageHandler implements Handler {
    private static final Pattern DIRECTORY_PATTERN = Pattern.compile("(.*?/)((\\d+)/$)?");
    protected final PageService pageService;
    protected final Site site;

    public PageHandler(Site site, PageService pageService) {
        this.pageService = pageService;
        this.site = site;
    }

    @Override
    public Response handle(Request request) throws IOException {
        if (isDirectory(request.path())) {
            return handleDirectory(request);
        } else {
            return handlePage(request);
        }
    }

    Response handleDirectory(Request request) throws IOException {
        Matcher matcher = DIRECTORY_PATTERN.matcher(request.path());
        matcher.matches();
        String path = matcher.group(1);
        int pageNumber = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2).substring(0, matcher.group(2).length() - 1));
        Optional<Page> directory = page(path, request);

        if (!directory.isPresent()) {
            throw new NotFoundException(request.path());
        }

        Resource template = template(directory.get());
        if (template == null) {
            throw new NotFoundException(directory.get().getTemplate());
        }

        Map<String, Object> context = Maps.newHashMap();
        context.put("page", new DirectoryPageObjectImpl(directory.get(), pageService, pageNumber));
        context.put("request", new RequestPageObjectImpl(request));
        context.put("site", new SitePageObjectImpl(site, pageService));
        return Response.template(template.path(), context);
    }

    Response handlePage(Request request) throws IOException {
        Optional<Page> page = page(request.path(), request);
        if (!page.isPresent()) {
            throw new NotFoundException(request.path());
        }
        Resource template = template(page.get());
        if (template == null) {
            throw new NotFoundException(page.get().getTemplate());
        }

        Map<String, Object> context = Maps.newHashMap();
        context.put("page", new PagePageObjectImpl(page.get(), pageService));
        context.put("request", new RequestPageObjectImpl(request));
        context.put("site", new SitePageObjectImpl(site, pageService));
        return Response.template(template.path(), context);
    }

    private boolean isDirectory(String path) {
        return path.endsWith("/");
    }

    protected Optional<Page> page(String path, Request request) {
        return pageService.findByPath(path);
    }

    protected Resource template(Page page) {
        return site.template().get(page.getTemplate());
    }
}
