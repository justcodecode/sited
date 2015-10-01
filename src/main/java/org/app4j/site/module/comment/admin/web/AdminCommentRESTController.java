package org.app4j.site.module.comment.admin.web;

import org.app4j.site.module.comment.service.CommentService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class AdminCommentRESTController {
    private final CommentService commentService;

    public AdminCommentRESTController(CommentService commentService) {
        this.commentService = commentService;
    }

    public Response findComments(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).get();
        int fetchSize = request.query("fetchSize", Integer.class).get();

        return Response.bean(commentService.find(offset, fetchSize));
    }
}
