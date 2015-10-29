package org.app4j.site.module.topic.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.internal.database.FindView;
import org.app4j.site.module.topic.domain.Reply;
import org.bson.Document;

/**
 * @author chi
 */
public class TopicService {
    private final MongoCollection<Reply> documents;

    public TopicService(MongoDatabase db) {
        this.documents = db.getCollection("Comment", Reply.class);
    }

    public FindView<Reply> find(int offset, int fetchSize) {
        FindView<Reply> replies = new FindView<>(offset, count());
        documents.find(new Document("status", 1)).sort(new Document("lastUpdateTime", -1)).skip(offset).limit(fetchSize).into(replies);
        return replies;
    }

    public long count() {
        return documents.count(new Document("status", 1));
    }
}
