package org.app4j.site.runtime.assets;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.google.common.hash.Hashing;

import java.io.IOException;

/**
 * @author chi
 */
public class Resource implements JsonSerializable {
    private final String path;
    private final byte[] content;

    public Resource(String path, byte[] content) {
        this.path = path;
        this.content = content;
    }

    public String path() {
        return path;
    }

    public byte[] content() {
        return content;
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
