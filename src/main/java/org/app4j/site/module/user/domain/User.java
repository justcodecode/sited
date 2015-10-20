package org.app4j.site.module.user.domain;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class User {
    public ObjectId id;
    public String username;
    public String email;
    public List<String> roles;
    public String password;
    public Date createTime;
    public Date lastUpdateTime;
    public Integer status;
}
