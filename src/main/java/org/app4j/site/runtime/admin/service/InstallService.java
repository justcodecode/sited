package org.app4j.site.runtime.admin.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * @author chi
 */
public class InstallService {
    private final MongoCollection<Document> documents;

    public InstallService(MongoDatabase db) {
        this.documents = db.getCollection("site.Install");
    }

    public boolean isInstalled() {
        return get() != null;
    }


    public Document get() {
        return documents.find().first();
    }

    public void installed(Date installTime) {
        documents.insertOne(new Document("intallTime", installTime));
    }
}
