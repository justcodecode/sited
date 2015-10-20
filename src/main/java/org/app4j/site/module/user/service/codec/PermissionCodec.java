package org.app4j.site.module.user.service.codec;

import org.app4j.site.module.user.domain.Permission;
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

public class PermissionCodec implements CollectibleCodec<Permission> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Permission decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);

        Permission permission = new Permission();

        permission.id = document.getObjectId("_id");
        permission.name = document.getString("name");
        permission.description = document.getString("description");
        permission.route = document.getString("route");
        permission.createTime = document.getDate("createTime");
        permission.lastUpdateTime = document.getDate("lastUpdateTime");
        permission.status = document.getInteger("status");

        return permission;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Permission permission, EncoderContext encoderContext) {
        Document document = new Document();
        if (permission.id != null) {
            document.put("_id", permission.id);
        }
        document.put("name", permission.name);
        document.put("description", permission.description);
        document.put("route", permission.route);
        document.put("createTime", permission.createTime);
        document.put("lastUpdateTime", permission.lastUpdateTime);
        document.put("status", permission.status);
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Permission> getEncoderClass() {
        return Permission.class;
    }

    @Override
    public Permission generateIdIfAbsentFromDocument(Permission document) {
        document.id = new ObjectId();
        return document;
    }

    @Override
    public boolean documentHasId(Permission document) {
        return document.id != null;
    }

    @Override
    public BsonValue getDocumentId(Permission document) {
        return new BsonObjectId(document.id);
    }
}