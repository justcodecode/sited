package org.app4j.site.module.comment;

import org.app4j.site.Module;
import org.app4j.site.module.comment.admin.web.AdminCommentRESTController;
import org.app4j.site.module.comment.codec.CommentCodec;
import org.app4j.site.module.comment.service.CommentService;

/**
 * @author chi
 */
public class CommentModule extends Module {
    @Override
    protected void configure() throws Exception {
        database().codecs().add(new CommentCodec());

        CommentService commentService = new CommentService(database().get());
        bind(CommentService.class).to(commentService);

        AdminCommentRESTController commentRESTController = new AdminCommentRESTController(commentService);
        admin().get("/admin/api/comment/", commentRESTController::findComments);
    }
}
