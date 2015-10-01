package org.app4j.site.module.user.codec;

import org.app4j.site.module.user.domain.Permission;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

/**
 * @author
 */
public class PermissionCodec implements Codec<Permission> {
    DocumentCodec documentCodec = new DocumentCodec();

    @SuppressWarnings("unchecked")
    @Override
    public Permission decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        Permission permission = new Permission();
        permission.setId(document.getObjectId("_id").toString());
        permission.setName(document.getString("name"));
        permission.setPath(document.getString("path"));
        permission.setCreateTime(document.getDate("createTime"));
        permission.setLastUpdateTime(document.getDate("lastUpdateTime"));
        permission.setStatus(document.getInteger("status"));

        return permission;
    }

    @Override
    public void encode(BsonWriter writer, Permission permission, EncoderContext encoderContext) {
        Document document = new Document();

        if (permission.getId() != null) {
            document.put("_id", new ObjectId(permission.getId()));
        }
        document.put("name", permission.getName());
        document.put("path", permission.getPath());
        document.put("createTime", permission.getCreateTime());
        document.put("lastUpdateTime", permission.getLastUpdateTime());
        document.put("status", permission.getStatus());

        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<Permission> getEncoderClass() {
        return Permission.class;
    }
}
