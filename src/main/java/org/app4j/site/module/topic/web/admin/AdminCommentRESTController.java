package org.app4j.site.module.topic.web.admin;

import org.app4j.site.module.topic.service.TopicService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminCommentRESTController {
    private final TopicService topicService;

    public AdminCommentRESTController(TopicService topicService) {
        this.topicService = topicService;
    }

    public Response findComments(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).get();
        int fetchSize = request.query("fetchSize", Integer.class).get();
        return Response.bean(topicService.find(offset, fetchSize));
    }
}
