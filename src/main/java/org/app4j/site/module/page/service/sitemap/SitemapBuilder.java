package org.app4j.site.module.page.service.sitemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class SitemapBuilder {
    private final String baseURL;
    private final Map<String, String> files = Maps.newHashMap();
    private final List<SitemapURLLoader> sitemapURLLoaders = Lists.newArrayList();

    public SitemapBuilder(String baseURL) {
        this.baseURL = baseURL;
    }

    public SitemapBuilder add(SitemapURLLoader sitemapURLLoader) {
        sitemapURLLoaders.add(sitemapURLLoader);
        return this;
    }

    public Map<String, String> build() {
        SitemapXml sitemapXml = new SitemapXml(baseURL);

        for (SitemapURLLoader sitemapURLLoader : sitemapURLLoaders) {
            for (SitemapURL sitemapURL : sitemapURLLoader) {
                sitemapXml.add(sitemapURL);

                if (sitemapXml.isFull()) {
                    String sitemapFile = "/sitemap/sitemap-" + files.size() + ".xml";
                    files.put(sitemapFile, sitemapXml.toXml());
                    sitemapXml = new SitemapXml(baseURL);
                }
            }
        }

        if (files.isEmpty()) {
            files.put("/sitemap.xml", sitemapXml.toXml());
        } else {
            String sitemapFile = "/sitemap/sitemap-" + files.size() + ".xml";
            files.put(sitemapFile, sitemapXml.toXml());
            files.put("/sitemap.xml", new SitemapIndexXml(baseURL, files).toXml());
        }

        return files;
    }
}
