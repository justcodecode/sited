package org.app4j.site.module.user.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class User implements JsonSerializable {
    public static final String COOKIE_NAME = "uid";

    public ObjectId id;
    public String username;
    public String email;
    public List<String> roles;
    public String password;
    public Date createTime;
    public Date lastUpdateTime;
    public Integer status;

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("username", username);
        gen.writeStringField("email", email);
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    }
}
