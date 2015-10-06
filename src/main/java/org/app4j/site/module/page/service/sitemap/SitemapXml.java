package org.app4j.site.module.page.service.sitemap;

import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author chi
 */
public class SitemapXml {
    private final List<SitemapURL> sitemapURLs = Lists.newArrayList();
    private final String baseURL;

    public SitemapXml(String baseURL) {
        this.baseURL = baseURL;
    }

    public void add(SitemapURL sitemapURL) {
        sitemapURLs.add(sitemapURL);
    }

    public boolean isEmpty() {
        return sitemapURLs.isEmpty();
    }

    public String toXml() {
        StringBuilder b = new StringBuilder(128);
        b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (SitemapURL sitemapURL : sitemapURLs) {
            b.append("<url><loc>").append(baseURL).append(sitemapURL.url()).append("</loc>");
            if (sitemapURL.updateFrequency() != null) {
                b.append("<changefreq>")
                        .append(sitemapURL.updateFrequency())
                        .append("</changefreq>");
            }

            if (sitemapURL.priority() != null) {
                b.append("<priority>").append(sitemapURL.priority()).append("</priority>");
            }

            if (sitemapURL.lastUpdateTime() != null) {
                b.append("<lastmod>").append(new SimpleDateFormat("yyyy-MM-dd").format(sitemapURL.lastUpdateTime())).append("</lastmod>");
            }

            b.append("</url>");
        }
        b.append("</urlset>");
        return b.toString();
    }

    public boolean isFull() {
        return sitemapURLs.size() >= 5000;
    }
}
