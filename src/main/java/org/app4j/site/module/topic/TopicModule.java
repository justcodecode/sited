package org.app4j.site.module.topic;

import org.app4j.site.Module;
import org.app4j.site.Site;
import org.app4j.site.module.topic.codec.QuickReplyCodec;
import org.app4j.site.module.topic.codec.ReplyCodec;
import org.app4j.site.module.topic.codec.TopicCodec;
import org.app4j.site.module.topic.service.TopicService;
import org.app4j.site.module.topic.web.admin.AdminCommentRESTController;

/**
 * @author chi
 */
public class TopicModule extends Module {
    public TopicModule(Site site) {
        super(site);
    }

    @Override
    protected void configure() throws Exception {
        database().codecs()
            .add(new ReplyCodec())
            .add(new QuickReplyCodec())
            .add(new TopicCodec());

        TopicService topicService = new TopicService(database().get());
        bind(TopicService.class).to(topicService);

        AdminCommentRESTController commentRESTController = new AdminCommentRESTController(topicService);
        admin().route().get("/web/admin/api/comment/", commentRESTController::findComments);
    }

}
