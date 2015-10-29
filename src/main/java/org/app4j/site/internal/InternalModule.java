package org.app4j.site.internal;

import org.app4j.site.Module;
import org.app4j.site.Site;

/**
 * @author chi
 */
public abstract class InternalModule extends Module {
    public InternalModule(Site site) {
        super(site);
        dependencies.clear();
    }
}
