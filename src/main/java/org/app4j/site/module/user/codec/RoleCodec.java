package org.app4j.site.module.user.codec;

import org.app4j.site.module.user.domain.Role;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * @author
 */
public class RoleCodec implements Codec<Role> {
    DocumentCodec documentCodec = new DocumentCodec();

    @SuppressWarnings("unchecked")
    @Override
    public Role decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        Role role = new Role();
        role.setId(document.getObjectId("_id").toString());
        role.setName(document.getString("name"));
        role.setPermissions((List<String>) document.get("permissions"));
        role.setCreateTime(document.getDate("createTime"));
        role.setLastUpdateTime(document.getDate("lastUpdateTime"));
        role.setStatus(document.getInteger("status"));

        return role;
    }

    @Override
    public void encode(BsonWriter writer, Role role, EncoderContext encoderContext) {
        Document document = new Document();

        if (role.getId() != null) {
            document.put("_id", new ObjectId(role.getId()));
        }
        document.put("name", role.getName());
        document.put("permissions", role.getPermissions());
        document.put("createTime", role.getCreateTime());
        document.put("lastUpdateTime", role.getLastUpdateTime());
        document.put("status", role.getStatus());

        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<Role> getEncoderClass() {
        return Role.class;
    }
}
