package org.app4j.site.module.topic.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.topic.domain.QuickReply;

import java.util.Date;

/**
 * @author chi
 */
public class QuickReplyService {
    private final MongoCollection<QuickReply> documents;

    public QuickReplyService(MongoDatabase db) {
        this.documents = db.getCollection("site.QuickReply", QuickReply.class);
    }

    public void save(QuickReply quickReply) {
        quickReply.status = 1;
        quickReply.createTime = new Date();
        documents.insertOne(quickReply);
    }
}
