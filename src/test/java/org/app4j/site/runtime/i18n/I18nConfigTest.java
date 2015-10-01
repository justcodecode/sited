package org.app4j.site.runtime.i18n;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author chi
 */
public class I18nConfigTest {
    I18nConfig i18nConfig;

    @Before
    public void setup() {
        i18nConfig = new I18nConfig();
        i18nConfig.add("en", "message/message.txt");

    }

    @Test
    public void message() {
        String message = i18nConfig.message("测试", "en");
        Assert.assertEquals("Test", message);
    }

}
