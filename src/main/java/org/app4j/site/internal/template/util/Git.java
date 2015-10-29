package org.app4j.site.internal.template.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * @author rainbow.cai
 */
public class Git {
    private final Logger logger = LoggerFactory.getLogger(Git.class);
    private final File dir;
    private String errors;
    private String updates;

    public Git(File dir) {
        Preconditions.checkState(dir.isDirectory(), "not a dir %s", dir);
        this.dir = dir;
    }

    public boolean isGitEnabled() {
        return new File(dir, ".git").exists();
    }

    public boolean pull() throws Exception {
        Process process = Runtime.getRuntime().exec("git " + " --git-dir=" + dir.getParentFile().getAbsolutePath() + "/.git pull origin master");

        try (InputStream errorStream = process.getErrorStream(); InputStream inputStream = process.getInputStream()) {
            errors = new String(ByteStreams.toByteArray(errorStream));
            updates = new String(ByteStreams.toByteArray(inputStream));
        }
        process.waitFor();
        return Strings.isNullOrEmpty(errors);
    }

    public String updates() {
        return updates;
    }

    public String errors() {
        return errors;
    }
}
