package org.app4j.site.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * @author chi
 */
public class SortedList<T> extends ArrayList<T> {
    private final Comparator<T> comparator;

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public synchronized boolean add(T t) {
        for (int i = 0; i < size(); i++) {
            T item = get(i);
            if (comparator.compare(item, t) > 0) {
                super.add(i, t);
                return true;
            }
        }
        return super.add(t);
    }

    @Override
    public void add(int index, T element) {
        throw new Error("not support");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            add(t);
        }
        return true;
    }
}
