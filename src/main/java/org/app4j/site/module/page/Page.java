package org.app4j.site.module.page;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class Page extends Document {
    public String id() {
        return getObjectId("_id").toHexString();
    }

    public void setId(String id) {
        put("_id", new ObjectId(id));
    }

    public String title() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String path() {
        return getString("path");
    }

    public void setPath(String path) {
        put("path", path);
    }

    public String templatePath() {
        return getString("templatePath");
    }

    public void setTemplatePath(String templatePath) {
        put("templatePath", templatePath);
    }

    public String description() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public Type type() {
        return path().endsWith("/") ? Type.DIRECTORY : Type.PAGE;
    }

    @SuppressWarnings("unchecked")
    public List<String> categories() {
        return get("categories", List.class);
    }

    public void setCategories(List<String> categories) {
        put("categories", categories);
    }

    public String name() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String content() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    @SuppressWarnings("unchecked")
    public List<String> tags() {
        return get("tags", List.class);
    }

    public void setTags(List<String> tags) {
        put("tags", tags);
    }

    public String language() {
        return getString("language");
    }

    public void setLanguage(String language) {
        put("language", language);
    }

    public String updateFrequency() {
        return getString("updateFrequency");
    }

    public void setUpdateFrequency(String updateFrequency) {
        put("updateFrequency", updateFrequency);
    }

    public String priority() {
        return getString("priority");
    }

    public void setPriority(String priority) {
        put("priority", priority);
    }

    public Date createTime() {
        return getDate("createTime");
    }

    public void setCreateTime(Date createTime) {
        put("createTime", createTime);
    }

    public Date lastUpdateTime() {
        return getDate("lastUpdateTime");
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        put("lastUpdateTime", lastUpdateTime);
    }

    public int status() {
        return getInteger("status");
    }

    public void setStatus(int status) {
        put("status", status);
    }

    public String author() {
        return getString("author");
    }

    public void setAuthor(String author) {
        put("author", author);
    }

    public Date publishTime() {
        return getDate("publishTime");
    }

    public void setPublishTime(Date publishTime) {
        put("publishTime", publishTime);
    }

    public String publisher() {
        return getString("publisher");
    }

    public void setPublisher(String publisher) {
        put("publisher", publisher);
    }

    public String subTitle() {
        return getString("subTitle");
    }

    public void setSubTitle(String subTitle) {
        put("subTitle", subTitle);
    }

    public String imageURL() {
        return getString("imageURL");
    }

    public void setImageURL(String imageURL) {
        put("imageURL", imageURL);
    }

    public boolean isDirectory() {
        return Type.DIRECTORY.equals(type());
    }


    public enum Type {
        PAGE, DIRECTORY
    }
}
