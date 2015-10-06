package org.app4j.site.runtime.database;

import org.bson.Document;

/**
 * @author chi
 */
public interface DomainCodec<T> {
    T from(Document doc);

    Document to(T object);

    default Class<T> domainType() {
        return null;
    }
}
