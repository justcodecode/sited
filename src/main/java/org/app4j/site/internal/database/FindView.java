package org.app4j.site.internal.database;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author chi
 */
public class FindView<T> extends ArrayList<T> implements Pageable<T>, JsonSerializable {
    private static final FindView<?> EMPTY = new FindView<>(0, 0);

    private final long offset;
    private final long total;

    public FindView(long offset, long total) {
        this.offset = offset;
        this.total = total;
    }

    @SuppressWarnings("unchecked")
    public static <T> FindView<T> empty() {
        return (FindView<T>) EMPTY;
    }

    @Override
    public long total() {
        return total;
    }

    @Override
    public long offset() {
        return offset;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("offset", offset);
        gen.writeNumberField("total", total);
        gen.writeArrayFieldStart("data");
        for (T object : this) {
            gen.writeObject(object);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    }
}
