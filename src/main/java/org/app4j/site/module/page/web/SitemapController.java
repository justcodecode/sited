package org.app4j.site.module.page.web;

import org.app4j.site.runtime.cache.service.DiskCache;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author chi
 */
public class SitemapController {
    private final DiskCache cache;

    public SitemapController(DiskCache cache) {
        this.cache = cache;
    }

    public Response sitemap(Request request) throws IOException {
        Optional<InputStream> inputStreamOptional = cache.get(request.path());
        if (!inputStreamOptional.isPresent()) {
            throw new NotFoundException(request.path());
        }
        return Response.pipe(inputStreamOptional.get()).setContentType("text/xml");
    }
}
