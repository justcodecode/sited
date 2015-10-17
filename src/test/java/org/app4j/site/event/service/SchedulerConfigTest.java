package org.app4j.site.event.service;

import org.app4j.site.runtime.scheduler.Event;
import org.app4j.site.runtime.scheduler.SchedulerModule;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author chi
 */
public class SchedulerConfigTest {
    @Test
    public void pop() {
        SchedulerModule schedulerConfig = new SchedulerModule(null);
        schedulerConfig.on(Object.class,
            event -> {
                Assert.assertNotNull(event.target());
            });
        Assert.assertTrue(schedulerConfig.trigger(new Event<>(new Object(), new HashMap<>())));
    }
}
