package org.app4j.site.runtime.database.shell;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.app4j.site.runtime.database.Pageable;
import org.bson.Document;

import java.util.Iterator;

/**
 * @author chi
 */
public class ShellCursor implements Pageable<Document>, ScriptObjectMirrorHelper {
    private final MongoCollection<Document> collection;
    private final Document filter;

    private Document sort = new Document();
    private int skip = 0;
    private int limit = 20;


    public ShellCursor(MongoCollection<Document> collection, Document filter) {
        this.collection = collection;
        this.filter = filter;
    }

    public long total() {
        return collection.count(filter);
    }

    public long offset() {
        return skip;
    }

    @Override
    public Iterator<Document> iterator() {
        FindIterable<Document> sort = collection.find(filter).skip(skip).limit(limit).sort(this.sort);
        return sort.iterator();
    }

    public ShellCursor skip(int offset) {
        this.skip = offset;
        return this;
    }

    public ShellCursor limit(int limit) {
        this.limit = limit;
        return this;
    }

    public ShellCursor sort(ScriptObjectMirror sort) {
        this.sort = toDocument(sort);
        return this;
    }
}
