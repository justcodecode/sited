package org.app4j.site.util;

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
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    FolderResourceRepository folderResourceRepository;

    @Before
    public void setup() throws IOException {
        File dir = temporaryFolder.newFolder("some");
        folderResourceRepository = new FolderResourceRepository(dir);
        new File(dir, "index.html").createNewFile();
    }

    @Test
    public void load() {
        Optional<Resource> resource = folderResourceRepository.resolve("/index.html");
        Assert.assertEquals("/index.html", resource.get().path());
        Assert.assertFalse(folderResourceRepository.resolve("/none.html").isPresent());
    }
}