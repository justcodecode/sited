package org.app4j.site.internal.event;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * @author chi
 */
public abstract class Task implements JsonSerializable, Runnable {
    public final String name;
    public State state;
    public double progress;
    public Throwable error;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", name);
        gen.writeStringField("state", state.name());
        gen.writeNumberField("progress", progress);
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    }

//    @Override
//    public void run() {
//        try {
//            if (state == State.WAIT) {
//                state = State.PROCESSING;
//
//                state = State.FINISHED;
//            }
//        } catch (Throwable e) {
//            error = e;
//            state = State.FAILED;
//        }
//    }

    public enum State {
        WAIT, PROCESSING, FINISHED, CANCELED, FAILED
    }
}
