package org.app4j.site.runtime.track;

import org.app4j.site.Module;
import org.app4j.site.runtime.track.service.TrackingCodec;
import org.app4j.site.runtime.track.service.TrackingService;

import java.util.Map;

/**
 * @author chi
 */
public class TrackModule extends Module implements TrackConfig {
    @Override
    public void configure() {
        site().database().codecs().add(new TrackingCodec());

        bind(TrackingService.class).to(new TrackingService(site().database().get())).export();
    }

    @Override
    public void track(String operator, String action, String target, Map<String, Object> context) {

    }
}
