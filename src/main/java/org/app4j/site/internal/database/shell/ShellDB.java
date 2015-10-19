package org.app4j.site.internal.database.shell;

import com.mongodb.client.MongoDatabase;
import jdk.nashorn.api.scripting.AbstractJSObject;

/**
 * @author chi
 */
public class ShellDB extends AbstractJSObject {
    private final MongoDatabase db;

    public ShellDB(MongoDatabase db) {
        this.db = db;
    }

    @Override
    public Object getMember(String name) {
        return new ShellCollection(db.getCollection(name));
    }

    @Override
    public boolean hasMember(String name) {
        return true;
    }
}
