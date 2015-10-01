package org.app4j.site.module.user.service;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.user.domain.User;
import org.app4j.site.runtime.database.FindView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * @author chi
 */
public class UserService {
    private final MongoCollection<User> documents;

    public UserService(MongoDatabase db) {
        this.documents = db.getCollection("cms.User", User.class);
    }

    public User findByUsername(String username) {
        return documents.find(new Document("username", username).append("status", 1)).first();
    }

    public FindView<User> findUsers(int offset, int fetchSize) {
        FindView<User> users = new FindView<>(offset, count());
        return documents.find(new Document("status", 1)).sort(new Document("lastUpdateTime", -1)).skip(offset).limit(fetchSize).into(users);
    }

    public long count() {
        return documents.count(new Document("status", 1));
    }

    public void insert(User user) {
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        user.setStatus(1);
        documents.insertOne(user);
    }

    public void update(User user) {
        Preconditions.checkNotNull(user.getId(), "%s missing id", user.getUsername());
        User old = findByUsername(user.getUsername());
        Date now = new Date();
        user.setCreateTime(old != null ? old.getCreateTime() : now);
        user.setLastUpdateTime(now);
        documents.replaceOne(new Document("_id", new ObjectId(user.getId())), user);
    }

    public String encode(User user) {
        return user.getUsername();
    }

    public User decode(String key) {
        return findByUsername(key);
    }

    public void delete(String id) {
        documents.updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("lastUpdateTime", new Date()).append("status", 0)));
    }
}
