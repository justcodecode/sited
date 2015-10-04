package org.app4j.site.runtime.assets;

import org.app4j.site.runtime.template.ClasspathResourceRepository;
import org.app4j.site.runtime.template.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author chi
 */
public class ClasspathResourceRepositoryTest {
    ClasspathResourceRepository classpathResourceLoader = new ClasspathResourceRepository("template", 100);

    @Test
    public void load() throws IOException {
        Resource resource = classpathResourceLoader.load("/href.html").get();
        Assert.assertEquals("/href.html", resource.path());
        Assert.assertFalse(classpathResourceLoader.load("/none.html").isPresent());
    }
}