package org.app4j.site.runtime.admin.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.runtime.admin.domain.Profile;
import org.bson.Document;

/**
 * @author chi
 */
public class ProfileService {
    private final MongoCollection<Profile> documents;

    public ProfileService(MongoDatabase db) {
        this.documents = db.getCollection("cms.Profile", Profile.class);
    }

    public boolean isInstalled() {
        return get() != null;
    }


    public Profile get() {
        return documents.find(new Document("status", 1)).first();
    }

    public void save(Profile profile) {
        documents.insertOne(profile);
    }
}
