package org.app4j.site.web;

import org.app4j.site.util.Asserts;
import org.junit.Test;

/**
 * @author chi
 */
public class AssertsTest {
    @Test
    public void asserts() {
        Asserts asserts = new Asserts();
        asserts.assertThat(new Object()).notNull();
    }

    @Test
    public void throwIfAny() {
        Asserts asserts = new Asserts();
        asserts.assertThat((String) null).is(Asserts.email())
                .assertThat(null).notNull().throwIfAny();
    }
}