package org.app4j.site.internal.route;


import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class Path {
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("(\\w+)(\\(.*\\))?");
    public final String path;
    public final List<String> variableNames = Lists.newArrayList();
    final List<Node> nodes = Lists.newArrayList();

    public Path(String path) {
        this.path = path;
        nodes.add(new Node("/"));
        StringBuilder b = new StringBuilder();

        int state = 0;
        for (int i = 1; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == ':') {
                state = 2;
            } else if (c == '*') {
                if (i != path.length() - 1) {
                    throw new Error("only support tailing wildcard");
                }
                nodes.add(new Wildcard());
            } else if (c == '/' || i == path.length() - 1) {
                if (c != '/' && i == path.length() - 1) {
                    b.append(c);
                }
                if (state == 2) {
                    nodes.add(pathVariable(b.toString()));
                    b.delete(0, b.length());
                } else if (state > 0) {
                    nodes.add(new Node(b.toString()));
                    b.delete(0, b.length());
                }
                if (c == '/' && i == path.length() - 1) {
                    nodes.add(new Node("/"));
                }
            } else {
                b.append(c);
                if (state == 0) {
                    state = 1;
                }
            }
        }
    }

    Node lastNode() {
        return nodes.get(nodes.size() - 1);
    }

    final PathVariable pathVariable(String path) {
        Matcher matcher = PATH_VARIABLE_PATTERN.matcher(path);
        if (!matcher.matches()) {
            throw new Error("invalid path variable " + path);
        }
        variableNames.add(matcher.group(1));
        if (matcher.group(2) != null) {
            return new PathVariable(matcher.group(2));
        } else {
            return new PathVariable(".*");
        }
    }

    static class Node {
        final String path;

        Node(String path) {
            this.path = path;
        }

        boolean matches(String path) {
            return this.path.equalsIgnoreCase(path);
        }

        public int priority() {
            return Integer.MAX_VALUE;
        }
    }

    static class PathVariable extends Node {
        final Pattern pattern;

        PathVariable(String regex) {
            super(regex);
            pattern = Pattern.compile(regex);
        }

        boolean matches(String path) {
            return pattern.matcher(path).matches();
        }

        public int priority() {
            return path.length();
        }
    }

    static class Wildcard extends Node {
        Wildcard() {
            super("*");
        }

        boolean matches(String path) {
            return true;
        }

        public int priority() {
            return 0;
        }
    }
}
