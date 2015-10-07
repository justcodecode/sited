package org.app4j.site.runtime.assets;

import org.app4j.site.util.ClasspathResourceRepository;
import org.app4j.site.util.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author chi
 */
public class ClasspathResourceRepositoryTest {
    ClasspathResourceRepository classpathResourceLoader = new ClasspathResourceRepository("/template");

    @Test
    public void load() throws IOException {
        Resource resource = classpathResourceLoader.resolve("/href.html").get();
        Assert.assertEquals("/href.html", resource.path());
        Assert.assertFalse(classpathResourceLoader.resolve("/none.html").isPresent());
    }
}