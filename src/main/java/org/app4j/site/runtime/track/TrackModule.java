package org.app4j.site.runtime.track;

import org.app4j.site.Module;
import org.app4j.site.runtime.track.service.TrackingCodec;
import org.app4j.site.runtime.track.service.TrackingService;

/**
 * @author chi
 */
public class TrackModule extends Module {
    @Override
    public void configure() {
        site().database().codecs().add(new TrackingCodec());

        bind(TrackingService.class).to(new TrackingService(site().database().get())).export();
    }

}
