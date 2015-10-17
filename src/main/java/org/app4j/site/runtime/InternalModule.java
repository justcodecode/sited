package org.app4j.site.runtime;

import com.google.common.collect.Lists;
import org.app4j.site.Module;
import org.app4j.site.Site;

import java.util.List;

/**
 * @author chi
 */
public abstract class InternalModule extends Module {
    public InternalModule(Site site) {
        super(site);
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Lists.newArrayList();
    }
}
