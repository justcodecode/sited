package org.app4j.site.internal.admin.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminUser implements JsonSerializable {
    public static final String ROLE_NAME = "admin";

    private final String username;
    private final String email;
    private final String password;

    public AdminUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("username", username());
        gen.writeStringField("email", email());
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    }
}
