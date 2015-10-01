package org.app4j.site.module.user.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.user.domain.Permission;
import org.app4j.site.runtime.database.FindView;
import org.bson.Document;

/**
 * @author
 */
public class PermissionService {
    private final MongoCollection<Permission> documents;

    public PermissionService(MongoDatabase db) {
        this.documents = db.getCollection("cms.Permission", Permission.class);
    }

    public FindView<Permission> listPermissions(int offset, int fetchSize) {
        return documents.find(new Document("status", 1)).skip(offset).limit(fetchSize).into(new FindView<>(offset, fetchSize));
    }

    public Permission findByName(String name) {
        return documents.find(new Document("name", name).append("status", 1)).first();
    }

    public void insert(Permission permission) {
        documents.insertOne(permission);
    }

    public void delete(String id) {
        documents.updateOne(new Document("_id", id), new Document("$set", new Document("status", 0)));
    }
}
