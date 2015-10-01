package org.app4j.site.module.track.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.track.domain.Tracking;
import org.bson.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class TrackingService {
    private final MongoCollection<Tracking> documents;

    public TrackingService(MongoDatabase db) {
        this.documents = db.getCollection("cms.Tracking", Tracking.class);
    }

    public List<Tracking> list(int offset, int fetchSize) {
        List<Tracking> results = Lists.newArrayList();
        documents.find().sort(new Document("createTime", -1)).skip(offset).limit(fetchSize).into(results);
        return results;
    }

    public void track(String actor, String action, String target) {
        track(actor, action, target, Maps.<String, Object>newHashMap());
    }

    public void track(String actor, String action, String target, Map<String, Object> context) {
        Tracking tracking = new Tracking();
        tracking.setActor(actor);
        tracking.setAction(action);
        tracking.setTarget(target);
        tracking.setContext(context);
        tracking.setCreateTime(new Date());
        tracking.setStatus(1);
        documents.insertOne(tracking);
    }

    public Tracking lastTracking(String actor, String action) {
        return documents.find().sort(new Document("createTime", -1)).limit(1).first();
    }

}
