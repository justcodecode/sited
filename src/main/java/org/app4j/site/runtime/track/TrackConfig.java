package org.app4j.site.runtime.track;

import java.util.Map;

/**
 * @author chi
 */
public interface TrackConfig {
    void track(String operator, String action, String target, Map<String, Object> context);
}
