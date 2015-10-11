package org.app4j.site.runtime.template.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.app4j.site.util.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author chi
 */
public class Template implements JsonSerializable {
    private final Resource resource;

    public Template(Resource resource) {
        this.resource = resource;
    }

    public String path() {
        return resource.path();
    }

    public String text() {
        return resource.text();
    }

    public Path resolve(String path) {
        return Paths.get(resource.path()).getParent().resolve(path).normalize();
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("path", path());
        jsonGenerator.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
    }
}
