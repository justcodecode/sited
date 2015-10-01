package org.app4j.site.module.page.service.index;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author chi
 */
public class DictionaryFilterTest {
    Dictionary dictionary;

    @Before
    public void setup() {
        dictionary = new Dictionary();
        dictionary.add("some english");
    }

    @Test
    public void exists() {
        Assert.assertTrue(dictionary.exists("some english"));
    }


}
