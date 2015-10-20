package org.app4j.site.module.user.service.codec;

import org.app4j.site.module.user.domain.User;
import org.bson.BsonObjectId;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.List;

public class UserCodec implements CollectibleCodec<User> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public User decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);

        User user = new User();

        user.id = document.getObjectId("_id");
        user.username = document.getString("username");
        user.email = document.getString("email");
        user.roles = document.get("roles", List.class);
        user.password = document.getString("password");
        user.createTime = document.getDate("createTime");
        user.lastUpdateTime = document.getDate("lastUpdateTime");
        user.status = document.getInteger("status");

        return user;
    }

    @Override
    public void encode(BsonWriter bsonWriter, User user, EncoderContext encoderContext) {
        Document document = new Document();
        if (user.id != null) {
            document.put("_id", user.id);
        }
        document.put("username", user.username);
        document.put("email", user.email);
        document.put("roles", user.roles);
        document.put("password", user.password);
        document.put("createTime", user.createTime);
        document.put("lastUpdateTime", user.lastUpdateTime);
        document.put("status", user.status);
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<User> getEncoderClass() {
        return User.class;
    }

    @Override
    public User generateIdIfAbsentFromDocument(User document) {
        document.id = new ObjectId();
        return document;
    }

    @Override
    public boolean documentHasId(User document) {
        return document.id != null;
    }

    @Override
    public BsonValue getDocumentId(User document) {
        return new BsonObjectId(document.id);
    }
}