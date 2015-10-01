package org.app4j.site.module.page.service.sitemap;

import java.text.SimpleDateFormat;

/**
 * @author chi
 */
public class SiteMapEntry {
    private String location;
    private String lastUpdateTime;

    public String toXml() {
        StringBuilder b = new StringBuilder(60);
        b.append("<sitemap><loc>")
            .append(location)
            .append("</loc>");


        if (lastUpdateTime != null) {
            b.append("<lastmod>")
                .append(new SimpleDateFormat("yyyy-MM-dd").format(lastUpdateTime))
                .append("</lastmod>");
        }

        b.append("</sitemap>");
        return b.toString();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
