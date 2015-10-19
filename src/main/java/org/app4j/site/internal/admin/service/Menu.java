package org.app4j.site.internal.admin.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author chi
 */
public class Menu {
    public List<Item> items = Lists.newArrayList();

    public Menu add(Item item) {
        items.add(item);
        return this;
    }

    public Menu add(String state, Item item) {
        Item parent = find(state);
        Preconditions.checkNotNull(parent, "missing parent state %s", state);
        parent.children.add(item);
        return this;
    }

    private Item find(String state) {
        for (Item item : items) {
            if (item.state.equals(state)) {
                return item;
            }
        }
        return null;
    }

    public static class Item {
        private final String label;
        private final String state;
        private final List<Item> children = Lists.newArrayList();

        public Item(String label, String state) {
            this.label = label;
            this.state = state;
        }

        public String getLabel() {
            return label;
        }

        public String getState() {
            return state;
        }

        public List<Item> getChildren() {
            return children;
        }
    }
}
