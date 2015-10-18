package org.app4j.site.module.page.domain;

import org.app4j.site.module.page.Page;
import org.app4j.site.util.JSON;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * @author chi
 */
public class PageTest {
    @Test
    public void json() {
        Page page = new Page();
        page.setId(new ObjectId().toHexString());
        page.put("some", "some");

        System.out.println(JSON.stringify(page));
    }
}