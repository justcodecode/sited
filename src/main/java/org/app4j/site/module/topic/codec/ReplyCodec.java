package org.app4j.site.module.topic.codec;

import org.app4j.site.module.topic.domain.Reply;
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

public class ReplyCodec implements CollectibleCodec<Reply> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Reply decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);

        Reply reply = new Reply();

        reply.id = document.getObjectId("_id");
        reply.username = document.getString("username");
        reply.topicId = document.getObjectId("topicId");
        reply.replied = document.getBoolean("replied");
        reply.content = document.getString("content");
        reply.createTime = document.getDate("createTime");
        reply.lastUpdateTime = document.getDate("lastUpdateTime");
        reply.status = document.getInteger("status");

        return reply;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Reply reply, EncoderContext encoderContext) {
        Document document = new Document();
        if (reply.id != null) {
            document.put("_id", reply.id);
        }
        document.put("username", reply.username);
        document.put("topicId", reply.topicId);
        document.put("replied", reply.replied);
        document.put("content", reply.content);
        document.put("createTime", reply.createTime);
        document.put("lastUpdateTime", reply.lastUpdateTime);
        document.put("status", reply.status);
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Reply> getEncoderClass() {
        return Reply.class;
    }

    @Override
    public Reply generateIdIfAbsentFromDocument(Reply document) {
        document.id = new ObjectId();
        return document;
    }

    @Override
    public boolean documentHasId(Reply document) {
        return document.id != null;
    }

    @Override
    public BsonValue getDocumentId(Reply document) {
        return new BsonObjectId(document.id);
    }
}