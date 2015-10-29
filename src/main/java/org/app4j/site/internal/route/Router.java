package org.app4j.site.internal.route;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.app4j.site.web.Handler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class Router {
    private final NodeRef root = new NodeRef(new Path.Node("/"));
    private final Map<Path.Node, Handler> targets = Maps.newHashMap();
    private final Map<Path.Node, Path> paths = Maps.newHashMap();

    public Router add(String route, Handler handler) {
        Path path = new Path(route);
        NodeRef current = root;
        for (int i = 1; i < path.nodes.size(); i++) {
            Path.Node node = path.nodes.get(i);
            current = current.findOrCreate(node);
        }
        paths.put(path.lastNode(), path);
        targets.put(path.lastNode(), handler);
        return this;
    }

    @SuppressWarnings("unchecked")
    public Optional<Route> find(String path) {
        Path p = new Path(path);

        if (p.nodes.size() == 1) {
            return route(root.node, Collections.EMPTY_LIST);
        }

        NodeRef current = root;
        List<String> variableValues = Lists.newArrayList();
        for (int i = 1; i < p.nodes.size(); i++) {
            Path.Node node = p.nodes.get(i);
            NodeRef next = current.find(node.path);
            if (next == null) {
                return Optional.empty();
            }
            current = next;
            if (next.isVariable()) {
                variableValues.add(node.path);
            } else if (next.isWildcard()) {
                break;
            }
        }
        return route(current.node, variableValues);
    }

    Optional<Route> route(Path.Node node, List<String> variableValues) {
        if (targets.containsKey(node)) {
            Route route = new Route();
            route.path = paths.get(node);
            route.handler = targets.get(node);
            route.variableValues = variableValues;
            return Optional.of(route);
        }
        return Optional.empty();
    }

    public static class Route {
        public Path path;
        public Handler handler;
        public List<String> variableValues;
    }

    public class NodeRef {
        Path.Node node;
        List<NodeRef> children = Lists.newArrayList();
        boolean variable;
        boolean wildcard;

        NodeRef(Path.Node node) {
            this.node = node;
            variable = node instanceof Path.PathVariable;
            wildcard = node instanceof Path.Wildcard;
        }

        public boolean isVariable() {
            return variable;
        }

        public boolean isWildcard() {
            return wildcard;
        }

        NodeRef findOrCreate(Path.Node node) {
            for (NodeRef child : children) {
                if (child.node.path.equals(node.path)) {
                    return child;
                }
            }
            NodeRef nodeRef = new NodeRef(node);
            int index = children.size();
            for (int i = 0; i < children.size(); i++) {
                if (node.priority() > children.get(i).node.priority()) {
                    index = i;
                    break;
                }
            }
            children.add(index, nodeRef);
            return nodeRef;
        }

        NodeRef find(String path) {
            for (NodeRef child : children) {
                if (child.node.matches(path)) {
                    return child;
                }
            }
            return null;
        }
    }
}
