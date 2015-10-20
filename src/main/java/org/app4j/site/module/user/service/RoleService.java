package org.app4j.site.module.user.service;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.internal.database.FindView;
import org.app4j.site.module.user.domain.Role;
import org.bson.Document;

import java.util.Date;

/**
 * @author
 */
public class RoleService {
    private final MongoCollection<Role> documents;

    public RoleService(MongoDatabase db) {
        this.documents = db.getCollection("cms.Role", Role.class);
    }

    public FindView<Role> listRoles(int offset, int fetchSize) {
        return documents.find(new Document("status", 1)).skip(offset).limit(fetchSize).into(new FindView<>(offset, fetchSize));
    }

    public Role findByName(String name) {
        return documents.find(new Document("name", name).append("status", 1)).first();
    }

    public void insert(Role role) {
        role.lastUpdateTime = new Date();
        role.createTime = new Date();
        role.status = 1;
        documents.insertOne(role);
    }

    public void update(Role role) {
        Preconditions.checkNotNull(role.id, "%s missing id", role.name);
        Role old = findByName(role.name);
        Date now = new Date();
        role.createTime = old != null ? old.createTime : now;
        role.lastUpdateTime = now;
        documents.replaceOne(new Document("_id", role.id), role);
    }

    public void delete(String id) {
        documents.updateOne(new Document("_id", id), new Document("$set", new Document("$set", new Document("lastUpdateTime", new Date()).append("status", 0))));
    }

    public void save(Role role) {
        role.createTime = new Date();
        role.lastUpdateTime = new Date();
        role.status = 1;
        documents.insertOne(role);
    }
}
