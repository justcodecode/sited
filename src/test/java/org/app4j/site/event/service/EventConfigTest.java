package org.app4j.site.event.service;

import com.google.common.collect.Maps;
import org.app4j.site.runtime.event.Event;
import org.app4j.site.runtime.event.EventConfig;
import org.app4j.site.runtime.event.EventModule;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chi
 */
public class EventConfigTest {
    @Test
    public void pop() {
        EventConfig eventConfig = new EventModule();
        eventConfig.on(Object.class,
                event -> {
                    System.out.println(event.target());
                });
        Assert.assertTrue(eventConfig.trigger(new Event<>(new Object(), Maps.<String, Object>newHashMap())));
    }
}
