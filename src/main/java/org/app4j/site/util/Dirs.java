package org.app4j.site.util;

import java.io.File;
import java.util.Stack;

public interface Dirs {
    static void create(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    static void delete(File dir) {
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