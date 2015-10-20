package org.app4j.site.module.topic.domain;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * @author chi
 */
public class Topic {
    public ObjectId id;
    public String category;
    public String title;
    public String content;
    public String username;
    public Date publishTime;
    public Date createTime;
    public Date lastUpdateTime;
    public Integer status;
}
