package org.app4j.site.module.comment.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.comment.domain.Comment;
import org.app4j.site.runtime.database.FindView;
import org.bson.Document;

/**
 * @author chi
 */
public class CommentService {
    private final MongoCollection<Comment> documents;

    public CommentService(MongoDatabase db) {
        this.documents = db.getCollection("Comment", Comment.class);
    }

    public FindView<Comment> find(int offset, int fetchSize) {
        FindView<Comment> comments = new FindView<>(offset, count());
        documents.find(new Document("status", 1)).sort(new Document("lastUpdateTime", -1)).skip(offset).limit(fetchSize).into(comments);
        return comments;
    }

    public long count() {
        return documents.count(new Document("status", 1));
    }
}
