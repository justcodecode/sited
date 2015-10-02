package org.app4j.site.module.page.web;

import com.google.common.base.Preconditions;
import org.app4j.site.web.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class PageRef {
    private static final Pattern DIRECTORY_PATTERN = Pattern.compile("(.*?/)((\\d+)/$)?");
    private final String path;
    private final String raw;
    private final Integer pageNumber;

    public PageRef(Request request) {
        Matcher matcher = DIRECTORY_PATTERN.matcher(request.path());
        if (matcher.matches()) {
            this.path = matcher.group(1);
            pageNumber = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2).substring(0, matcher.group(2).length() - 1));
        } else {
            this.path = request.path();
            pageNumber = null;
        }
        this.raw = path;
    }

    public String path() {
        return path;
    }

    public String raw() {
        return raw;
    }

    public String template() {
        if (isDirectory()) {
            if (path.equals("/")) {
                return "/index.html";
            } else {
                return path.substring(0, path.length() - 1) + ".html";
            }
        } else {
            return path;
        }
    }

    public boolean isDirectory() {
        return path.endsWith("/");
    }

    public Integer pageNumber() {
        Preconditions.checkNotNull(pageNumber, "%s is not directory page", path);
        return pageNumber;
    }
}
