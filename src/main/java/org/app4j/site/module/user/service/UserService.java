package org.app4j.site.module.user.service;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.internal.database.FindView;
import org.app4j.site.module.user.domain.User;
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

    public boolean isUsernameOrEmailExists(String username, String email) {
        return findByUsername(username).isPresent() || findByEmail(email).isPresent();
    }

    public Optional<User> findByUsername(String username) {
        User user = documents.find(new Document("username", username).append("status", 1)).first();
        return user == null ? Optional.empty() : Optional.of(user);
    }

    public Optional<User> findByEmail(String email) {
        User user = documents.find(new Document("email", email).append("status", 1)).first();
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

    public String encodeUser(User user) {
        return user.username;
    }

    public User decodeUser(String token) {
        return findByUsername(token).get();
    }

    public Optional<User> user(Request request) {
        Value<String> username = request.cookie(USER_COOKIE_NAME);
        if (username.isPresent()) {
            return findByUsername(username.get());
        }
        return Optional.empty();
    }

    public void insert(User user) {
        user.createTime = new Date();
        user.lastUpdateTime = new Date();
        user.status = 1;
        documents.insertOne(user);
    }

    public void update(User user) {
        Preconditions.checkNotNull(user.id, "%s missing id", user.username);
        User old = findByUsername(user.username).get();
        Date now = new Date();
        user.createTime = old != null ? old.createTime : now;
        user.lastUpdateTime = now;
        documents.replaceOne(new Document("_id", user.id), user);
    }

    public void delete(String id) {
        documents.updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("lastUpdateTime", new Date()).append("status", 0)));
    }

    public void save(User user) {
        documents.insertOne(user);
    }
}
