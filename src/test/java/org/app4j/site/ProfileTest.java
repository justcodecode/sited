package org.app4j.site;

import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author chi
 */
public class ProfileTest {
    @Test
    public void get() {
        Profile profile = new Profile();
        profile.put("some", "some");
        Property<String> property = profile.property("site.some", String.class);
        Assert.assertEquals("some", property.get());
    }

    @Test
    public void loadFromYAML() {
        try (InputStream in = Resources.getResource("test.yml").openStream()) {
            Profile profile = Profile.loadFromYAML(CharStreams.toString(new InputStreamReader(in)));
            Assert.assertEquals("some", profile.property("site.name", String.class).get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}