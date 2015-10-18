package org.app4j.site.module.page.variable;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class PagePath {
    private static final Pattern DIRECTORY_PATTERN = Pattern.compile("(.*?/)((\\d+)/$)?");
    private final String pagePath;
    private final String requestPath;
    private final Integer index;

    public PagePath(String pagePath) {
        Matcher matcher = DIRECTORY_PATTERN.matcher(pagePath);
        if (matcher.matches()) {
            this.pagePath = matcher.group(1);
            index = matcher.group(2) == null ? 0 : Integer.parseInt(matcher.group(2).substring(0, matcher.group(2).length() - 1));
        } else {
            this.pagePath = pagePath;
            index = null;
        }
        this.requestPath = pagePath;
    }

    public String pagePath() {
        return pagePath;
    }

    public String requestPath() {
        return requestPath;
    }

    public String templatePath() {
        if (isDirectory()) {
            if ("/".equals(pagePath)) {
                return "/index.html";
            } else {
                return pagePath.substring(0, pagePath.length() - 1) + ".html";
            }
        } else {
            return pagePath;
        }
    }

    public boolean isDirectory() {
        return pagePath.endsWith("/");
    }

    public Integer index() {
        Preconditions.checkNotNull(index, "%s is not directory page", pagePath);
        return index;
    }
}
