package org.app4j.site.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author chi
 */
public class Resource implements JsonSerializable {
    private final String path;
    private final Supplier<InputStream> supplier;

    public Resource(String path, Supplier<InputStream> supplier) {
        Preconditions.checkState(path.startsWith("/"), "path must start with /");
        this.path = path;
        this.supplier = supplier;
    }

    public String path() {
        return path;
    }

    public InputStream openStream() {
        return supplier.get();
    }

    public String text() {
        return new String(bytes(), Charsets.UTF_8);
    }

    public byte[] bytes() {
        try (InputStream inputStream = openStream()) {
            return ByteStreams.toByteArray(inputStream);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public String md5() {
        return Hashing.md5().hashBytes(bytes()).toString();
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("path", path);
        jsonGenerator.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
    }
}
