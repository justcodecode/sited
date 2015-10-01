package org.app4j.site;

import org.app4j.site.web.Parameter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chi
 */
public class ParameterTest {
    @Test
    public void some() {
        Parameter<Long> parameter = new Parameter<>("name", 1L);
        parameter.assertThat(value -> value > 0);
        Assert.assertEquals(1, (long) parameter.get());
    }
}
