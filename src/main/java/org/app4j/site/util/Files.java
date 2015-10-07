package org.app4j.site.util;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

public interface Files {
    static void createDirIfNoneExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    static void createParentDirs(File file) {
        try {
            com.google.common.io.Files.createParentDirs(file);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    static void deleteDir(File dir) {
        Stack<File> stack = new Stack<>();
        stack.push(dir);
        while (!stack.isEmpty()) {
            File current = stack.pop();
            if (current.listFiles() != null) {
                stack.add(current);
                for (File file : current.listFiles()) {
                    if (file.isDirectory()) {
                        stack.add(file);
                    } else {
                        file.delete();
                    }
                }
            } else {
                current.delete();
            }
        }
    }
}