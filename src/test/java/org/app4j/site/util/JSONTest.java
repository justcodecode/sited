package org.app4j.site.util;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chi
 */
public class JSONTest {
    @Test
    public void objectId() {
        ObjectId objectId = new ObjectId("5531effd1b92e6ef4da65a47");
        Assert.assertEquals("\"5531effd1b92e6ef4da65a47\"", JSON.stringify(objectId));
    }


    @Test
    public void convertValue() {
        Assert.assertEquals(1, (int) JSON.mapper().convertValue("1", int.class));
    }
}
