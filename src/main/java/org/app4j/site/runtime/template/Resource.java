package org.app4j.site.runtime.template;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
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
    private final Supplier<InputStream> streamSupplier;

    public Resource(String path, Supplier<InputStream> streamSupplier) {
        this.path = path;
        this.streamSupplier = streamSupplier;
    }

    public String path() {
        return path;
    }

    public InputStream inputStream() {
        return streamSupplier.get();
    }


    public byte[] content() {
        try (InputStream inputStream = inputStream()) {
            return ByteStreams.toByteArray(inputStream);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public String md5() {
        return Hashing.md5().hashBytes(content()).toString();
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
