package org.app4j.site.module.user.domain;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class Role {
    public ObjectId id;
    public String name;
    public String description;
    public List<String> permissions;
    public Date createTime;
    public Date lastUpdateTime;
    public Integer status;
}
