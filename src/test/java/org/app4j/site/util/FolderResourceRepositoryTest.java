package org.app4j.site.util;

import org.app4j.site.runtime.template.FolderResourceRepository;
import org.app4j.site.runtime.template.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * @author chi
 */
public class FolderResourceRepositoryTest {
    @Rule
    public TemporaryFolder dir = new TemporaryFolder();

    FolderResourceRepository folderResourceRepository;


    @Before
    public void setup() throws IOException {
        File root = this.dir.getRoot();
        folderResourceRepository = new FolderResourceRepository(root);

        dir.newFile("index.html").createNewFile();
    }

    @Test
    public void load() {
        Optional<Resource> resource = folderResourceRepository.load("index.html");
        Assert.assertEquals("index.html", resource.get().path());
        Assert.assertFalse(folderResourceRepository.load("none.html").isPresent());
    }
}