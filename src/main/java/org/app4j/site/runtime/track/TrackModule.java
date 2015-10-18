package org.app4j.site.runtime.track;

import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.runtime.SiteModule;
import org.app4j.site.runtime.database.DatabaseModule;
import org.app4j.site.runtime.track.service.TrackingCodec;
import org.app4j.site.runtime.track.service.TrackingService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class TrackModule extends SiteModule implements TrackConfig {
    public TrackModule(Site site) {
        super(site);
    }

    @Override
    public List<Class<? extends Module>> dependencies() {
        return Arrays.asList(DatabaseModule.class);
    }

    @Override
    public void configure() {
        bind(TrackConfig.class).to(this).export();

        site().database().codecs().add(new TrackingCodec());
        bind(TrackingService.class).to(new TrackingService(site().database().get())).export();
    }

    @Override
    public void track(String operator, String action, String target, Map<String, Object> context) {
    }
}
