package org.app4j.site.module.question.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.question.domain.Answer;
import org.app4j.site.runtime.database.FindView;
import org.bson.Document;

import java.util.Date;

/**
 * @author chi
 */
public class AnswerService {
    private final MongoCollection<Answer> documents;

    public AnswerService(MongoDatabase db) {
        documents = db.getCollection("Answer", Answer.class);
    }

    public FindView<Answer> findByQuestionId(String questionId, int offset, int fetchSize) {
        FindView<Answer> results = new FindView<>(offset, count(questionId));
        documents.find(new Document("questionId", questionId).append("status", 1))
                .sort(new Document("createTime", -1))
                .limit(fetchSize)
                .into(results);
        return results;
    }

    public long count(String questionId) {
        return documents.count(new Document("questionId", questionId).append("status", 1));
    }

    public void save(Answer answer) {
        answer.setStatus(1);
        answer.setLastUpdateTime(new Date());
        answer.setCreateTime(new Date());
        documents.insertOne(answer);
    }
}
