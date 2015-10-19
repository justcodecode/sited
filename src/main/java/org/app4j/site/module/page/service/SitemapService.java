package org.app4j.site.module.page.service;

import com.google.common.base.Charsets;
import org.app4j.site.module.page.Page;
import org.app4j.site.module.page.service.sitemap.SitemapBuilder;
import org.app4j.site.module.page.service.sitemap.SitemapURL;
import org.app4j.site.module.page.service.sitemap.SitemapURLLoader;
import org.app4j.site.internal.cache.service.DiskCache;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * @author chi
 */
public class SitemapService {
    private final String baseURL;
    private final PageService pageService;
    private final DiskCache cache;

    public SitemapService(String baseURL, PageService pageService, DiskCache cache) {
        this.baseURL = baseURL;
        this.pageService = pageService;
        this.cache = cache;
    }

    public void rebuild() {
        SitemapBuilder sitemapBuilder = new SitemapBuilder(baseURL);
        sitemapBuilder.add(new SitemapURLLoader() {
            Iterator<Page> dumper = pageService.dumper().iterator();

            @Override
            public Iterator<SitemapURL> iterator() {
                return new Iterator<SitemapURL>() {
                    @Override
                    public boolean hasNext() {
                        return dumper.hasNext();
                    }

                    @Override
                    public SitemapURL next() {
                        Page page = dumper.next();
                        return new SitemapURL(page.path(), page.updateFrequency(), page.lastUpdateTime(), page.priority());
                    }
                };
            }
        });

        Map<String, String> files = sitemapBuilder.build();
        files.forEach((path, content) -> cache.put(path, new ByteArrayInputStream(content.getBytes(Charsets.UTF_8))));
    }
}
