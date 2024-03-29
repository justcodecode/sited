package org.app4j.site.event.service;

import org.app4j.site.internal.event.Event;
import org.app4j.site.internal.event.EventModule;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author chi
 */
public class EventConfigTest {
    @Test
    public void pop() {
        EventModule schedulerConfig = new EventModule(null);
        schedulerConfig.on(Object.class,
            event -> Assert.assertNotNull(event.target));
        Assert.assertTrue(schedulerConfig.trigger(new Event<>(new Object(), new HashMap<>())));
    }
}
