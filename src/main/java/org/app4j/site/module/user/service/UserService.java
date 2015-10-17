package org.app4j.site.module.user.service;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.user.domain.User;
import org.app4j.site.runtime.database.FindView;
import org.app4j.site.util.Value;
import org.app4j.site.web.Request;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Optional;

/**
 * @author chi
 */
public class UserService {
    private static final String USER_COOKIE_NAME = "uid";
    private final MongoCollection<User> documents;

    public UserService(MongoDatabase db) {
        this.documents = db.getCollection("site.User", User.class);
    }

    public Optional<User> findByUsername(String username) {
        User user = documents.find(new Document("username", username).append("status", 1)).first();
        return user == null ? Optional.empty() : Optional.of(user);
    }

    public Optional<User> findByUserId(String id) {
        User user = documents.find(new Document("_id", new ObjectId(id)).append("status", 1)).first();
        return user == null ? Optional.empty() : Optional.of(user);
    }

    public FindView<User> findUsers(int offset, int fetchSize) {
        FindView<User> users = new FindView<>(offset, count());
        return documents.find(new Document("status", 1)).sort(new Document("lastUpdateTime", -1)).skip(offset).limit(fetchSize).into(users);
    }

    public long count() {
        return documents.count(new Document("status", 1));
    }

    public Optional<User> user(Request request) {
        Value<String> userId = request.cookie(USER_COOKIE_NAME);
        if (userId.isPresent()) {
            return findByUserId(userId.get());
        }
        return Optional.empty();
    }

    public void insert(User user) {
        user.setCreateTime(new Date());
        user.setLastUpdateTime(new Date());
        user.setStatus(1);
        documents.insertOne(user);
    }

    public void update(User user) {
        Preconditions.checkNotNull(user.getId(), "%s missing id", user.getUsername());
        User old = findByUsername(user.getUsername()).get();
        Date now = new Date();
        user.setCreateTime(old != null ? old.getCreateTime() : now);
        user.setLastUpdateTime(now);
        documents.replaceOne(new Document("_id", new ObjectId(user.getId())), user);
    }

    public String encode(User user) {
        return user.getUsername();
    }

    public User decode(String key) {
        return findByUsername(key).get();
    }

    public void delete(String id) {
        documents.updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("lastUpdateTime", new Date()).append("status", 0)));
    }

    public void save(User user) {
        documents.insertOne(user);
    }
}
