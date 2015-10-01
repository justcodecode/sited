package org.app4j.site;

import org.junit.Test;

/**
 * @author chi
 */
public class StandardExceptionTest {
    @Test
    public void message() {
        StandardException e = new StandardException("some {}", "some", new RuntimeException());
        e.printStackTrace();
    }
}
