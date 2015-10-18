package org.app4j.site.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author chi
 */
public class SortedListTest {
    @Test
    public void sort() {
        SortedList<Integer> list = new SortedList<>((o1, o2) -> o1 - o2);
        list.add(3);
        list.add(1);
        list.add(2);
        Assert.assertEquals(1, (int) list.get(0));
    }
}