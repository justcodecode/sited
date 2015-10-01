package org.app4j.site.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * @author chi
 */
public final class JSON {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();

        objectMapper.setDateFormat(new ISO8601DateFormat());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new JsonSerializer<ObjectId>() {
            @Override
            public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.toString());
            }
        });

        objectMapper.registerModule(module);
    }

    public static ObjectMapper mapper() {
        return objectMapper;
    }

    public static <T> String stringify(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
