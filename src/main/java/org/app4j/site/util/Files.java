package org.app4j.site.util;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public interface Files {
    static void createDirIfNoneExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    static Iterator<File> iterate(File dir) {
        Deque<File> stack = Lists.newLinkedList();
        stack.add(dir);

        return new Iterator<File>() {
            File current;

            @Override
            public boolean hasNext() {
                while (!stack.isEmpty() && current == null) {
                    File file = stack.pollLast();
                    if (file.isFile()) {
                        current = file;
                    } else {
                        if (file.listFiles() != null) {
                            for (File f : file.listFiles()) {
                                stack.addLast(f);
                            }
                        }
                    }
                }
                return current != null;
            }

            @Override
            public File next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                File next = current;
                current = null;
                return next;
            }
        };
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