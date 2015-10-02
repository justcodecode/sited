package org.app4j.site.module.page.service.sitemap;

import org.app4j.site.module.page.domain.Page;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * @author chi
 */
public class SiteMapUrlEntry {
    private final String baseUrl;
    private final Page page;

    public SiteMapUrlEntry(String baseUrl, Page dbObject) {
        this.baseUrl = baseUrl;
        this.page = dbObject;
    }


    public String toXml() {
        StringBuilder b = new StringBuilder(64);
        try {
            b.append("<url><loc>")
                    .append(new URL(baseUrl + '/' + page.getPath()).toURI().normalize().toString())
                    .append("</loc>");
        } catch (MalformedURLException | URISyntaxException e) {
            throw new Error(e);
        }

        if (page.getUpdateFrequency() != null) {
            b.append("<changefreq>")
                    .append(page.getUpdateFrequency())
                    .append("</changefreq>");
        }

        if (page.getPriority() != null) {
            b.append("<priority>").append(page.getPriority()).append("</priority>");
        }

        if (page.getLastUpdateTime() != null) {
            b.append("<lastmod>").append(new SimpleDateFormat("yyyy-MM-dd").format(page.getLastUpdateTime())).append("</lastmod>");
        }

        b.append("</url>");
        return b.toString();
    }
}
