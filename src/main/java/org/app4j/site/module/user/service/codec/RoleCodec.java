package org.app4j.site.module.user.service.codec;

import org.app4j.site.module.user.domain.Role;
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

public class RoleCodec implements CollectibleCodec<Role> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Role decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);

        Role role = new Role();

        role.id = document.getObjectId("_id");
        role.name = document.getString("name");
        role.description = document.getString("description");
        role.permissions = document.get("permissions", List.class);
        role.createTime = document.getDate("createTime");
        role.lastUpdateTime = document.getDate("lastUpdateTime");
        role.status = document.getInteger("status");

        return role;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Role role, EncoderContext encoderContext) {
        Document document = new Document();
        if (role.id != null) {
            document.put("_id", role.id);
        }
        document.put("name", role.name);
        document.put("description", role.description);
        document.put("permissions", role.permissions);
        document.put("createTime", role.createTime);
        document.put("lastUpdateTime", role.lastUpdateTime);
        document.put("status", role.status);
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Role> getEncoderClass() {
        return Role.class;
    }

    @Override
    public Role generateIdIfAbsentFromDocument(Role document) {
        document.id = new ObjectId();
        return document;
    }

    @Override
    public boolean documentHasId(Role document) {
        return document.id != null;
    }

    @Override
    public BsonValue getDocumentId(Role document) {
        return new BsonObjectId(document.id);
    }
}