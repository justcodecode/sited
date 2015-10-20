package org.app4j.site.module.topic.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.internal.database.FindView;
import org.app4j.site.module.topic.domain.Reply;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * @author chi
 */
public class ReplyService {
    private final MongoCollection<Reply> documents;

    public ReplyService(MongoDatabase db) {
        this.documents = db.getCollection("site.Reply", Reply.class);
    }

    public FindView<Reply> findByTopicId(ObjectId topicId, int offset, int fetchSize) {
        FindView<Reply> results = new FindView<>(offset, total(topicId));
        documents.find(new Document("topicId", topicId).append("status", 1))
            .skip(offset).limit(fetchSize)
            .into(results);
        return results;
    }

    public int total(ObjectId topicId) {
        return (int) documents.count(new Document("topicId", topicId).append("status", 1));
    }

    public void save(Reply reply) {
        reply.lastUpdateTime = new Date();
        reply.createTime = new Date();
        reply.status = 1;
        documents.insertOne(reply);
    }
}
