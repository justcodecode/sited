package org.app4j.site.module.page.service.sitemap;

import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.PageService;
import org.app4j.site.module.track.service.TrackingService;
import org.app4j.site.runtime.database.FindView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

/**
 * @author chi
 */
public class SiteMapService {
    private final Logger logger = LoggerFactory.getLogger(SiteMapService.class);
    private final String baseUrl;
    private PageService pageService;
    private TrackingService trackingService;
    private Cache<String, byte[]> cache;

    public SiteMapService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public SiteMapService setPageService(PageService pageService) {
        this.pageService = pageService;
        return this;
    }

    public SiteMapService setTrackingService(TrackingService trackingService) {
        this.trackingService = trackingService;
        return this;
    }

    public SiteMapService setCache(Cache<String, byte[]> cache) {
        this.cache = cache;
        return this;
    }

    public String siteMap(String path) {
        byte[] xml = cache.getIfPresent(path);

        if (xml == null) {
            create();
            xml = cache.getIfPresent(path);
        }

        if (xml == null) {
            return null;
        }

        return new String(xml, Charsets.UTF_8);
    }

    public void create() {
        logger.info("create sitemap");
        List<String> sitemapFiles = Lists.newArrayList();
        SiteMapXml siteMapXml = new SiteMapXml();

        final int FETCH_SIZE = 1000;
        int offset = 0;
        int fetched;
        FindView<Page> pages;
        do {
            fetched = 0;
            pages = pageService.find(offset, FETCH_SIZE);

            for (Page page : pages) {
                siteMapXml.addUrlEntry(new SiteMapUrlEntry(baseUrl, page));

                fetched++;
                if (siteMapXml.isFull()) {
                    String sitemapFile = "/sitemap/sitemap-" + sitemapFiles.size() + ".xml";
                    cache.put(sitemapFile, siteMapXml.toXml().getBytes(Charsets.UTF_8));
                    siteMapXml = new SiteMapXml();
                    sitemapFiles.add(sitemapFile);
                }
            }

            offset += fetched;
        } while (fetched == FETCH_SIZE);

        if (sitemapFiles.isEmpty()) {
            cache.put("/sitemap.xml", siteMapXml.toXml().getBytes(Charsets.UTF_8));
        } else {
            String sitemapFile = "/sitemap/sitemap-" + sitemapFiles.size() + ".xml";
            cache.put(sitemapFile, siteMapXml.toXml().getBytes(Charsets.UTF_8));

            //Create index file
            SiteMapIndexXml siteMapIndexXml = new SiteMapIndexXml();
            for (String file : sitemapFiles) {
                try {
                    siteMapIndexXml.add(new URL(baseUrl + '/' + file).toURI().toString());
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
            cache.put("/sitemap.xml", siteMapIndexXml.toXml().getBytes(Charsets.UTF_8));
        }
        trackingService.track("system", "create", "site");
    }
}
