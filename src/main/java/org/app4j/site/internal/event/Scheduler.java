package org.app4j.site.internal.event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author chi
 */
public interface Scheduler {
    Scheduler execute(Task task);

    Scheduler schedule(LocalDateTime time, ChronoUnit unit, Task task);
}
