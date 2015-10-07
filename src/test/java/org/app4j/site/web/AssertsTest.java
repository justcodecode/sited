package org.app4j.site.web;

import org.app4j.site.util.Asserts;
import org.app4j.site.util.Value;
import org.app4j.site.web.exception.BadRequestException;
import org.junit.Test;

/**
 * @author chi
 */
public class AssertsTest {
    @Test
    public void asserts() {
        Asserts asserts = new Asserts();
        asserts.assertThat(new Value<>("some", new Object())).notNull();
    }

    @Test(expected = BadRequestException.class)
    public void throwIfAny() {
        Asserts asserts = new Asserts();
        asserts.assertThat(new Value<>("some", (String) null)).is(Asserts.email())
                .assertThat(new Value<>("some", null)).notNull().throwIfAny();
    }
}