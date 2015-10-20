package org.app4j.site.module.topic.domain;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * @author chi
 */
public class QuickReply {
    public ObjectId id;
    public ObjectId replyId;
    public String username;
    public String content;
    public Date createTime;
    public Integer status;
}
