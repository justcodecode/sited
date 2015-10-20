package org.app4j.site.module.topic.domain;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class Reply {
    public ObjectId id;
    public String username;
    public ObjectId topicId;
    public Boolean replied;
    public String content;
    public Date createTime;
    public Date lastUpdateTime;
    public Integer status;

    public transient List<QuickReply> replies;
}
