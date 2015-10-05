package org.app4j.site.runtime.admin.codec;

import org.app4j.site.runtime.admin.domain.Profile;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

/**
 * @author chi
 */
public class ProfileCodec implements Codec<Profile> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    public Profile decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);
        Profile profile = new Profile();
        profile.setId(document.getObjectId("_id").toHexString());
        profile.setLocale(document.getString("locale"));
        profile.setCharset(document.getString("charset"));
        profile.setCreateTime(document.getDate("createTime"));
        profile.setLastUpdateTime(document.getDate("lastUpdateTime"));
        profile.setStatus(document.getInteger("status"));
        return profile;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Profile profile, EncoderContext encoderContext) {
        Document document = new Document();
        if (profile.getId() != null) {
            document.put("_id", new ObjectId(profile.getId()));
        }
        document.put("locale", profile.getLocale());
        document.put("charset", profile.getCharset());
        document.put("createTime", profile.getCreateTime());
        document.put("lastUpdateTime", profile.getLastUpdateTime());
        document.put("status", profile.getStatus());

        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Profile> getEncoderClass() {
        return Profile.class;
    }
}
