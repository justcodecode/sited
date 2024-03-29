package org.app4j.site.module.page.web.admin;

import org.app4j.site.internal.event.EventConfig;
import org.app4j.site.internal.event.Task;
import org.app4j.site.module.page.Page;
import org.app4j.site.module.page.service.PageIndexService;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminPageRESTController {
    //    private final Index<Page> index;
    private final PageIndexService pageIndexService;
    private final PageService pageService;
    private final EventConfig eventConfig;

    public AdminPageRESTController(PageService pageService, PageIndexService pageIndexService, EventConfig eventConfig) {
        this.pageIndexService = pageIndexService;
        this.pageService = pageService;
        this.eventConfig = eventConfig;
    }

    public Response getPage(Request request) throws IOException {
        String id = request.path(":id").get();
        Page page = pageService.findById(new ObjectId(id));
        if (page == null) {
            throw new NotFoundException(request.path());
        }
        return Response.bean(page);
    }

    public Response findPages(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(20).get();
        return Response.bean(pageService.find(offset, fetchSize));
    }

    public Response createPage(Request request) throws IOException {
        Page page = request.body(Page.class);
        pageService.saveOrUpdate(page);
        return Response.bean(page);
    }

    public Response updatePage(Request request) throws IOException {
        Page page = request.body(Page.class);
        pageService.saveOrUpdate(page);
        return Response.empty();
    }

    public Response deletePage(Request request) throws IOException {
        String pageId = request.path(":id").get();
        pageService.deletePage(new ObjectId(pageId));
        return Response.empty();
    }

    public Response rebuildIndex(Request request) {
        eventConfig.scheduler().execute(new Task("rebuild index") {
            @Override
            public void run() {
                pageIndexService.rebuild();
            }
        });
        return Response.empty();
    }
}
