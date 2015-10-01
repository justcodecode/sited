package org.app4j.site.module.page.service.sitemap;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author chi
 */
public class SiteMapXml {
    private final List<SiteMapUrlEntry> urlEntries = Lists.newArrayList();

    public void addUrlEntry(SiteMapUrlEntry urlEntry) {
        urlEntries.add(urlEntry);
    }

    public boolean isEmpty() {
        return urlEntries.isEmpty();
    }

    public String toXml() {
        StringBuilder b = new StringBuilder(128);
        b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (SiteMapUrlEntry urlEntry : urlEntries) {
            b.append(urlEntry.toXml());
        }
        b.append("</urlset>");
        return b.toString();
    }

    public boolean isFull() {
        return urlEntries.size() >= 5000;
    }
}
