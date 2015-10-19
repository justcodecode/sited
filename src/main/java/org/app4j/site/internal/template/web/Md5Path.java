package org.app4j.site.internal.template.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Md5Path {
    static final Pattern HASHED_NAME = Pattern.compile("(.+)\\.([a-fA-F0-9]{32})\\.(.{2,4})$");

    private final String path;
    private String md5;

    public Md5Path(String fullPath) {
        int p = fullPath.lastIndexOf('/');
        if (p > 0 && p < fullPath.length() - 1) {
            String name = fullPath.substring(p + 1);
            Matcher matcher = HASHED_NAME.matcher(name);

            if (matcher.matches()) {
                path = fullPath.substring(0, p + 1) + matcher.group(1) + "." + matcher.group(3);
                md5 = matcher.group(2);
            } else {
                path = fullPath;
            }
        } else {
            path = fullPath;
        }
    }

    public Md5Path(String path, String md5) {
        this.path = path;
        this.md5 = md5;
    }

    public String hashedPath() {
        int end = path.lastIndexOf('.');

        if (end > 0) {
            return path.substring(0, end) + '.' + md5 + path.substring(end);
        } else {
            return path;
        }
    }

    public String path() {
        return path;
    }
}
