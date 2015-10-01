package org.app4j.site.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author chi
 */
public class Graph<T> implements Iterable<T> {
    private final Map<T, List<T>> dependencies = Maps.newHashMap();
    private boolean strictDependency;

    public Graph<T> add(T target, List<T> dependencies) {
        this.dependencies.put(target, dependencies);

        if (!strictDependency) {
            dependencies.stream().filter(dependency -> !this.dependencies.containsKey(dependency)).forEach(dependency -> {
                this.dependencies.put(dependency, Lists.<T>newArrayList());
            });
        }
        return this;
    }

    public Graph<T> enableStrictDependency() {
        strictDependency = true;
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        validate();

        final Set<T> visited = Sets.newHashSet();
        final Queue<T> queue = new LinkedList<>(dependencies.keySet());

        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public T next() {
                while (true) {
                    T target = queue.poll();

                    boolean satisfied = true;

                    for (T dependency : dependencies.get(target)) {
                        if (!visited.contains(dependency)) {
                            satisfied = false;
                            break;
                        }
                    }
                    if (satisfied) {
                        visited.add(target);
                        return target;
                    } else {
                        queue.add(target);
                    }
                }
            }

            @Override
            public void remove() {
                throw new RuntimeException("not implemented");
            }
        };
    }

    void validate() {
        Map<T, Set<T>> overall = Maps.newHashMap();

        for (Map.Entry<T, List<T>> entry : dependencies.entrySet()) {
            if (!overall.containsKey(entry.getKey())) {
                overall.put(entry.getKey(), Sets.<T>newHashSet());
            }

            for (T dependency : entry.getValue()) {
                overall.get(entry.getKey()).add(dependency);

                if (overall.containsKey(dependency)) {
                    overall.get(entry.getKey()).addAll(overall.get(dependency));
                }

                Preconditions.checkState(dependencies.containsKey(dependency), "missing dependency, %s -> %s", entry.getKey(), dependency);
            }

            Preconditions.checkState(!overall.get(entry.getKey()).contains(entry.getKey()), "cycle dependency, %", entry.getKey());
        }
    }

    public Iterable<T> reverse() {
        return Lists.reverse(Lists.newArrayList(iterator()));
    }
}
