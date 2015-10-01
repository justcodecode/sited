package org.app4j.site.runtime.route;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Node {
    final String path;
    final List<Node> children = new ArrayList<>();

    Node(String path) {
        this.path = path;
    }

    protected Node add(Path path, List<String> variableNames) {
        Path next = path.next;

        if (next == null) {
            return this;
        } else {
            for (Node child : children) {
                if (child.matches(next.path)) {
                    return child.add(next, variableNames);
                }
            }

            Node child = create(next.path);
            return addChild(child).add(next, variableNames);
        }
    }

    Node create(String path) {
        if (path.startsWith(":")) {
            return new Variable(path);
        } else if ("*".equals(path)) {
            return new Wildcard();
        } else {
            return new Node(path);
        }
    }

    Node addChild(Node node) {
        int i;
        int priority = node.getClass().equals(Node.class) ? 3 : node.getClass().equals(Variable.class) ? 2 : 1;

        for (i = 0; i < children.size(); i++) {
            Node child = children.get(i);

            int childPriority = child.getClass().equals(Node.class) ? 3 : child.getClass().equals(Variable.class) ? 2 : 1;

            if (childPriority < priority || childPriority == priority && child.path.length() < node.path.length()) {
                break;
            }
        }
        children.add(i, node);
        return node;
    }

    boolean matches(String path) {
        return this.path.equals(path);
    }

    Node find(Path path, List<String> parameters) {
        if (matches(path.path)) {
            if (path.next == null) {
                return this;
            }
            for (Node child : children) {
                Node found = child.find(path.next, parameters);
                if (found != null) {
                    return found;
                }
            }

        }
        return null;
    }

    static class Path {
        final String path;
        Path next;

        public Path(String path) {
            int p = path.indexOf('/');

            if (p >= 0) {
                this.path = path.substring(0, p + 1);

                if (p < path.length() - 1) {
                    this.next = new Path(path.substring(p + 1));
                }
            } else {
                this.path = path;
            }
        }
    }

    static class Variable extends Node {
        static final Pattern VARIABLE_PATTERN = Pattern.compile(":(\\w*)(\\(.*\\))?(.*)?");
        final Pattern pattern;

        Variable(String path) {
            super(path);
            Matcher matcher = VARIABLE_PATTERN.matcher(path);
            if (matcher.matches()) {
                if (matcher.group(2) != null) {
                    pattern = Pattern.compile(matcher.group(2) + (matcher.group(3) == null ? "" : matcher.group(3)));
                } else {
                    pattern = Pattern.compile("(.*)");
                }
            } else {
                throw new RuntimeException("invalid path " + path);
            }
        }

        protected Node add(Path path, List<String> variableNames) {
            Path next = path.next;
            if (next == null) {
                Matcher matcher = VARIABLE_PATTERN.matcher(path.path);
                if (matcher.matches()) {
                    variableNames.add(':' + matcher.group(1));
                }
                return this;
            } else {
                for (Node child : children) {
                    if (child.matches(next.path)) {
                        return child.add(next, variableNames);
                    }
                }
                Node child = create(next.path);
                return addChild(child).add(next, variableNames);
            }
        }

        boolean matches(String path) {
            return pattern.pattern().equals(path);
        }

        Node find(Path path, List<String> parameters) {
            Matcher matcher = pattern.matcher(path.path);
            if (matcher.matches()) {
                if (path.next == null) {
                    for (int i = matcher.groupCount(); i > 0; i--) {
                        parameters.add(matcher.group(i));
                    }
                    return this;
                }
                for (Node child : children) {
                    Node found = child.find(path.next, parameters);

                    if (found != null) {
                        for (int j = matcher.groupCount(); j > 0; j--) {
                            parameters.add(matcher.group(j));
                        }

                        return found;
                    }
                }
            }
            return null;
        }

    }

    static class Wildcard extends Node {
        Wildcard() {
            super("*");
        }

        Node find(Path path, List<String> parameters) {
            return this;
        }
    }
}
