package org.app4j.site.module.page.service.sitemap;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author chi
 */
public class SiteMapIndexXml {
    private final List<String> sitemapIndexUrls = Lists.newArrayList();

    public void add(String url) {
        sitemapIndexUrls.add(url);
    }

    public String toXml() {
        StringBuilder b = new StringBuilder(200);
        b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (String sitemapIndexUrl : sitemapIndexUrls) {
            b.append("<sitemap><loc>")
                .append(sitemapIndexUrl)
                .append("</loc></sitemap>");
        }
        b.append("</sitemapindex>");
        return b.toString();
    }
}
