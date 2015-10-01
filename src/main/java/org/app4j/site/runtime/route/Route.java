package org.app4j.site.runtime.route;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class Route<T> {
    private final Map<Node, T> targets = new HashMap<>();
    private final Map<Node, List<String>> variableNames = new HashMap<>();
    private final Node root = new Node("/");

    public Route add(String route, T target) {
        Node.Path path = new Node.Path(route);
        List<String> variableNames = Lists.newArrayList();
        Node node = root.add(path, variableNames);

        if (targets.containsKey(node)) {
            throw new RuntimeException(String.format("route %s exists", route));
        }
        targets.put(node, target);
        this.variableNames.put(node, variableNames);
        return this;
    }


    public T find(String path, Map<String, String> parameters) {
        List<String> parameterValues = new ArrayList<>();
        Node node = root.find(new Node.Path(path), parameterValues);

        if (node == null) {
            return null;
        }

        T target = targets.get(node);
        if (target != null && variableNames.containsKey(node)) {
            List<String> names = variableNames.get(node);
            for (int i = 0; i < names.size(); i++) {
                parameters.put(names.get(i), parameterValues.get(parameterValues.size() - 1 - i));
            }
        }
        return target;
    }
}
