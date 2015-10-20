package org.app4j.site.module.topic.codec;

import org.app4j.site.module.topic.domain.Topic;
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

public class TopicCodec implements CollectibleCodec<Topic> {
    private final DocumentCodec documentCodec = new DocumentCodec();

    @Override
    @SuppressWarnings("unchecked")
    public Topic decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(bsonReader, decoderContext);

        Topic topic = new Topic();

        topic.id = document.getObjectId("_id");
        topic.category = document.getString("category");
        topic.title = document.getString("title");
        topic.content = document.getString("content");
        topic.username = document.getString("username");
        topic.publishTime = document.getDate("publishTime");
        topic.createTime = document.getDate("createTime");
        topic.lastUpdateTime = document.getDate("lastUpdateTime");
        topic.status = document.getInteger("status");

        return topic;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Topic topic, EncoderContext encoderContext) {
        Document document = new Document();
        if (topic.id != null) {
            document.put("_id", topic.id);
        }
        document.put("category", topic.category);
        document.put("title", topic.title);
        document.put("content", topic.content);
        document.put("username", topic.username);
        document.put("publishTime", topic.publishTime);
        document.put("createTime", topic.createTime);
        document.put("lastUpdateTime", topic.lastUpdateTime);
        document.put("status", topic.status);
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Topic> getEncoderClass() {
        return Topic.class;
    }

    @Override
    public Topic generateIdIfAbsentFromDocument(Topic document) {
        document.id = new ObjectId();
        return document;
    }

    @Override
    public boolean documentHasId(Topic document) {
        return document.id != null;
    }

    @Override
    public BsonValue getDocumentId(Topic document) {
        return new BsonObjectId(document.id);
    }
}