package org.app4j.site.module.page.sitemap;

import java.util.Date;

/**
 * @author chi
 */
public class SitemapURL {
    private final String path;
    private final String updateFrequency;
    private final Date lastUpdateTime;
    private final String priority;

    public SitemapURL(String path, String updateFrequency, Date lastUpdateTime, String priority) {
        this.path = path;
        this.updateFrequency = updateFrequency;
        this.lastUpdateTime = lastUpdateTime;
        this.priority = priority;
    }

    public String url() {
        return path;
    }

    public String updateFrequency() {
        return updateFrequency;
    }

    public Date lastUpdateTime() {
        return lastUpdateTime;
    }

    public String priority() {
        return priority;
    }
}
