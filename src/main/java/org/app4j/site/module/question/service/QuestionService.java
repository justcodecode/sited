package org.app4j.site.module.question.service;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.question.domain.Question;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class QuestionService {
    private final MongoCollection<Question> documents;

    public QuestionService(MongoDatabase db) {
        documents = db.getCollection("Question", Question.class);
    }

    public List<Question> findByTag(String tag) {
        ArrayList<Question> results = Lists.newArrayList();
        documents.find(new Document("tags", tag).append("status", 1)).limit(10).into(results);
        return results;
    }

    public Question findById(String id) {
        return documents.find(new Document("_id", new ObjectId(id)).append("status", 1)).first();
    }

    public void save(Question question) {
        question.setStatus(1);
        question.setCreateTime(new Date());
        question.setLastUpdateTime(new Date());
        documents.insertOne(question);
    }
}
