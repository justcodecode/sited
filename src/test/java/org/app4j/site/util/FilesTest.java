package org.app4j.site.util;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author chi
 */
public class FilesTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();


    @Before
    public void setup() throws IOException {
        File dir = temporaryFolder.newFolder("folder");
        new File(dir, "1.file").createNewFile();
        new File(dir, "2.file").createNewFile();

        File dir2 = new File(dir, "some");
        dir2.mkdirs();
        new File(dir2, "2.file").createNewFile();
    }


    @Test
    public void allFiles() {
        ArrayList<File> files = Lists.newArrayList(Files.allFiles(temporaryFolder.getRoot()));
        Assert.assertEquals(3, files.size());
    }
}