package org.app4j.site.module.topic.codec;

import org.app4j.site.module.topic.domain.QuickReply;
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

public class QuickReplyCodec implements CollectibleCodec<QuickReply> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public QuickReply decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);

        QuickReply quickReply = new QuickReply();

        quickReply.id = document.getObjectId("_id");
        quickReply.replyId = document.getObjectId("replyId");
        quickReply.username = document.getString("username");
        quickReply.content = document.getString("content");
        quickReply.createTime = document.getDate("createTime");
        quickReply.status = document.getInteger("status");

        return quickReply;
    }

    @Override
    public void encode(BsonWriter bsonWriter, QuickReply quickReply, EncoderContext encoderContext) {
        Document document = new Document();
        if (quickReply.id != null) {
            document.put("_id", quickReply.id);
        }
        document.put("replyId", quickReply.replyId);
        document.put("username", quickReply.username);
        document.put("content", quickReply.content);
        document.put("createTime", quickReply.createTime);
        document.put("status", quickReply.status);
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<QuickReply> getEncoderClass() {
        return QuickReply.class;
    }

    @Override
    public QuickReply generateIdIfAbsentFromDocument(QuickReply document) {
        document.id = new ObjectId();
        return document;
    }

    @Override
    public boolean documentHasId(QuickReply document) {
        return document.id != null;
    }

    @Override
    public BsonValue getDocumentId(QuickReply document) {
        return new BsonObjectId(document.id);
    }
}