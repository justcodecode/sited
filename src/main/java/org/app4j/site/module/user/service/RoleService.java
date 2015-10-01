package org.app4j.site.module.user.service;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.user.domain.Role;
import org.app4j.site.runtime.database.FindView;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        role.setLastUpdateTime(new Date());
        role.setCreateTime(new Date());
        role.setStatus(1);
        documents.insertOne(role);
    }

    public void update(Role role) {
        Preconditions.checkNotNull(role.getId(), "%s missing id", role.getName());
        Role old = findByName(role.getName());
        Date now = new Date();
        role.setCreateTime(old != null ? old.getCreateTime() : now);
        role.setLastUpdateTime(now);
        documents.replaceOne(new Document("_id", new ObjectId(role.getId())), role);
    }

    public void delete(String id) {
        documents.updateOne(new Document("_id", id), new Document("$set", new Document("$set", new Document("lastUpdateTime", new Date()).append("status", 0))));
    }
}
