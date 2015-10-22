package org.app4j.site.module.page.web;

import org.app4j.site.module.page.variable.PageVariable;
import org.app4j.site.module.page.variable.RequestVariable;
import org.app4j.site.module.page.variable.SiteVariable;

import java.util.HashMap;

/**
 * @author chi
 */
public class PageContext extends HashMap<String, Object> {
    public boolean hasPage() {
        return containsKey("page") && get("page") != null;
    }

    public PageVariable.Page page() {
        return (PageVariable.Page) get("page");
    }

    public SiteVariable.Site site() {
        return (SiteVariable.Site) get("site");
    }

    public RequestVariable.Request request() {
        return (RequestVariable.Request) get("request");
    }
}
