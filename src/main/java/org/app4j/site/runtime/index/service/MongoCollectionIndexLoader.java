package org.app4j.site.runtime.index.service;

import com.mongodb.client.MongoCollection;

import java.util.Iterator;

/**
 * @author chi
 */
public class MongoCollectionIndexLoader<T> implements IndexLoader<T> {
    private final MongoCollection<T> documents;

    public MongoCollectionIndexLoader(MongoCollection<T> documents) {
        this.documents = documents;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int offset = 0;
            Iterator<T> results = documents.find().skip(offset).limit(10000).iterator();

            @Override
            public boolean hasNext() {
                if (results.hasNext()) {
                    return true;
                }

                offset += 10000;
                results = documents.find().skip(offset).limit(10000).iterator();
                return results.hasNext();
            }

            @Override
            public T next() {
                return results.next();
            }
        };
    }
}
