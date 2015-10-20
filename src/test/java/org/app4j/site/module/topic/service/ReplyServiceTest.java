package org.app4j.site.module.topic.service;

import com.github.fakemongo.junit.FongoRule;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.internal.database.SimpleCodecRegistry;
import org.app4j.site.module.topic.codec.ReplyCodec;
import org.app4j.site.module.topic.domain.Reply;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author chi
 */
public class ReplyServiceTest {
    @Rule
    public FongoRule fongoRule = new FongoRule();
    ReplyService replyService;

    @Before
    public void setup() {
        SimpleCodecRegistry codecRegistry = new SimpleCodecRegistry();
        codecRegistry.add(new ReplyCodec());
        MongoDatabase database = fongoRule.getDatabase().withCodecRegistry(codecRegistry);
        replyService = new ReplyService(database);
    }

    @Test
    public void save() {
        Reply reply = new Reply();
        reply.topicId = new ObjectId();
        reply.content = "content";
        replyService.save(reply);
        Assert.assertNotNull(reply.id);
    }
}