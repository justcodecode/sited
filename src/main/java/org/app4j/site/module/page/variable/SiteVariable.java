package org.app4j.site.module.page.variable;

import org.app4j.site.Scope;

/**
 * @author chi
 */
public class SiteVariable implements Variable<SiteVariable.Site> {
    private final org.app4j.site.Site site;

    public SiteVariable(org.app4j.site.Site site) {
        this.site = site;
    }

    @Override
    public Site eval(VariableRef variableRef, Scope scope) {
        Site siteObject = new Site();
        siteObject.name = site.name();
        siteObject.description = site.description();
        siteObject.logoURL = site.logoURL();
        return siteObject;
    }

    public static class Site {
        public String name;
        public String description;
        public String logoURL;
    }
}
