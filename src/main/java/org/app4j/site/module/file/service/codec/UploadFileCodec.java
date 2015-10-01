package org.app4j.site.module.file.service.codec;

import org.app4j.site.module.file.domain.UploadFile;
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
public class UploadFileCodec implements Codec<UploadFile> {
    DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public UploadFile decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);
        UploadFile uploadFile = new UploadFile();
        uploadFile.setId(document.getObjectId("_id").toHexString());
        uploadFile.setPath(document.getString("path"));
        uploadFile.setDescription(document.getString("description"));
        uploadFile.setTitle(document.getString("title"));
        uploadFile.setTags(document.get("tags", List.class));
        uploadFile.setCreateTime(document.getDate("createTime"));
        uploadFile.setLastUpdateTime(document.getDate("lastUpdateTime"));
        uploadFile.setStatus(document.getInteger("status"));
        return uploadFile;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void encode(BsonWriter bsonWriter, UploadFile uploadFile, EncoderContext encoderContext) {
        Document document = new Document();
        if (uploadFile.getId() != null) {
            document.put("_id", new ObjectId(uploadFile.getId()));
        }
        document.put("path", uploadFile.getPath());
        document.put("description", uploadFile.getDescription());
        document.put("title", uploadFile.getTitle());
        document.put("tags", uploadFile.getTags());
        document.put("createTime", uploadFile.getCreateTime());
        document.put("lastUpdateTime", uploadFile.getLastUpdateTime());
        document.put("status", uploadFile.getStatus());
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<UploadFile> getEncoderClass() {
        return UploadFile.class;
    }
}
