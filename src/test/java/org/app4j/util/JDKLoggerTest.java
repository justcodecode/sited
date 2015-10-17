package org.app4j.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chi
 */
public class JDKLoggerTest {
    private final Logger logger = LoggerFactory.getLogger(JDKLoggerTest.class);

    @Test
    public void info() {
        logger.debug("some");
    }
}
