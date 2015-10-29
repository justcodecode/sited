package org.app4j.site.internal.track;

import org.app4j.site.Site;
import org.app4j.site.internal.InternalModule;
import org.app4j.site.internal.database.DatabaseModule;
import org.app4j.site.internal.track.service.TrackingCodec;
import org.app4j.site.internal.track.service.TrackingService;

import java.util.Map;

/**
 * @author chi
 */
public class TrackModule extends InternalModule implements TrackConfig {
    public TrackModule(Site site) {
        super(site);
        dependencies.add(DatabaseModule.class);
    }

    @Override
    public void configure() {
        bind(TrackConfig.class).to(this).export();
        site.database().codecs().add(new TrackingCodec());
        bind(TrackingService.class).to(new TrackingService(site.database().get())).export();
    }

    @Override
    public void track(String operator, String action, String target, Map<String, Object> context) {
    }
}
