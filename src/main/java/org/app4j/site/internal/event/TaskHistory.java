package org.app4j.site.internal.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * @author chi
 */
public class TaskHistory implements JsonSerializable {
    private final Task task;

    public TaskHistory(Task task) {
        this.task = task;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", task.name());
        gen.writeStringField("state", task.state().name());
        gen.writeNumberField("progress", task.progress());
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {

    }
}
