package org.app4j.site.module.topic.web;

import org.app4j.site.internal.database.FindView;
import org.app4j.site.module.topic.domain.QuickReply;
import org.app4j.site.module.topic.domain.Reply;
import org.app4j.site.module.topic.service.QuickReplyService;
import org.app4j.site.module.topic.service.ReplyService;
import org.app4j.site.module.topic.service.TopicService;
import org.app4j.site.module.user.domain.User;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * @author chi
 */
public class TopicRESTController {
    private final TopicService topicService;
    private final ReplyService replyService;
    private final QuickReplyService quickReplyService;

    public TopicRESTController(TopicService topicService, ReplyService replyService, QuickReplyService quickReplyService) {
        this.topicService = topicService;
        this.replyService = replyService;
        this.quickReplyService = quickReplyService;
    }

    public Response findReplies(Request request) {
        String topicId = request.path(":topicId").get();
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(20).get();
        FindView<Reply> results = replyService.findByTopicId(new ObjectId(topicId), offset, fetchSize);
        return Response.bean(results);
    }

    public Response createReply(Request request) throws IOException {
        String topicId = request.path(":topicId").get();
        Reply reply = request.body(Reply.class);
        reply.topicId = new ObjectId(topicId);
        replyService.save(reply);
        return Response.bean(reply);
    }

    public Response createQuickReply(Request request) throws IOException {
        String topicId = request.path(":topicId").get();
        QuickReply quickReply = request.body(QuickReply.class);
        User user = request.require(User.class);
        quickReply.username = user.username;
        quickReplyService.save(quickReply);
        return Response.bean(quickReply);
    }
}
