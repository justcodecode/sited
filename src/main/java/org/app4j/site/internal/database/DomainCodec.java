package org.app4j.site.internal.database;

import org.bson.Document;

import java.lang.reflect.ParameterizedType;

/**
 * @author chi
 */
public interface DomainCodec<T> {
    T from(Document doc);

    Document to(T object);

    @SuppressWarnings("unchecked")
    default Class<T> domainType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }
}
