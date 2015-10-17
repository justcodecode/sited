package org.app4j.site.util;

import org.junit.Test;

import java.util.Comparator;

/**
 * @author chi
 */
public class SortedListTest {
    @Test
    public void sort() {
        SortedList<Integer> list = new SortedList<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        list.add(3);
        list.add(1);
        list.add(2);

        System.out.println(list);
    }
}