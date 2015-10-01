package org.app4j.site.module.user.codec;

import org.app4j.site.module.user.domain.User;
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
 * @author chi
 */
public class UserCodec implements Codec<User> {
    DocumentCodec documentCodec = new DocumentCodec();

    @SuppressWarnings("unchecked")
    @Override
    public User decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        User user = new User();
        user.setId(document.getObjectId("_id").toString());
        user.setUsername(document.getString("username"));
        user.setEmail(document.getString("email"));
        user.setRoles((List<String>) document.get("roles"));
        user.setPassword(document.getString("password"));
        user.setCreateTime(document.getDate("createTime"));
        user.setLastUpdateTime(document.getDate("lastUpdateTime"));
        user.setStatus(document.getInteger("status"));

        return user;
    }

    @Override
    public void encode(BsonWriter writer, User user, EncoderContext encoderContext) {
        Document document = new Document();

        if (user.getId() != null) {
            document.put("_id", new ObjectId(user.getId()));
        }
        document.put("username", user.getUsername());
        document.put("email", user.getEmail());
        document.put("roles", user.getRoles());
        document.put("password", user.getPassword());
        document.put("createTime", user.getCreateTime());
        document.put("lastUpdateTime", user.getLastUpdateTime());
        document.put("status", user.getStatus());

        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<User> getEncoderClass() {
        return User.class;
    }
}
