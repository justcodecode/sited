package org.app4j.site.module.page.sitemap;

import java.util.Map;

/**
 * @author chi
 */
public class SitemapIndexXml {
    private final String baseURL;
    private final Map<String, String> files;

    public SitemapIndexXml(String baseURL, Map<String, String> files) {
        this.baseURL = baseURL;
        this.files = files;
    }

    public String toXml() {
        StringBuilder b = new StringBuilder(200);
        b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (String sitemapFileName : files.keySet()) {
            b.append("<sitemap><loc>")
                    .append(baseURL).append(sitemapFileName)
                    .append("</loc></sitemap>");

        }
        b.append("</sitemapindex>");
        return b.toString();
    }
}
