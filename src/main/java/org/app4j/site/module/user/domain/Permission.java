package org.app4j.site.module.user.domain;

import org.bson.types.ObjectId;

import java.util.Date;

/**
 * @author chi
 */
public class Permission {
    public ObjectId id;
    public String name;
    public String description;
    public String route;
    public Date createTime;
    public Date lastUpdateTime;
    public Integer status;
}
