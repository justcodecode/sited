package org.app4j.site.runtime.database.shell;

import com.mongodb.client.MongoCollection;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.bson.Document;

/**
 * @author chi
 */
public class ShellCollection implements ScriptObjectMirrorHelper {
    public final String name;
    private final MongoCollection<Document> collection;

    public ShellCollection(MongoCollection<Document> collection) {
        this.name = collection.getNamespace().getCollectionName();
        this.collection = collection;
    }

    public ShellCursor find() {
        return new ShellCursor(collection, new Document());
    }

    public ShellCursor find(ScriptObjectMirror filter) {
        return new ShellCursor(collection, toDocument(filter));
    }

    public Document findOne(ScriptObjectMirror filter) {
        Document document = toDocument(filter);
        return collection.find(document).first();
    }
}
