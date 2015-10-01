package org.app4j.site.event.service;

import com.google.api.client.util.Maps;
import org.app4j.site.runtime.event.Event;
import org.app4j.site.runtime.event.EventConfig;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chi
 */
public class EventConfigTest {
    @Test
    public void pop() {
        EventConfig eventConfig = new EventConfig();

        eventConfig.addListener(Object.class,
            event -> {
                System.out.println(event.target());
            });

        Assert.assertTrue(eventConfig.pop(new Event<>("some", new Object(), Maps.<String, Object>newHashMap())));
    }
}
