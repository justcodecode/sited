package org.app4j.site.module.page.domain;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class Page extends Document {
    public String getId() {
        return getObjectId("_id").toHexString();
    }

    public void setId(String id) {
        put("_id", new ObjectId(id));
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getPath() {
        return getString("path");
    }

    public void setPath(String path) {
        put("path", path);
    }

    public String getTemplate() {
        return getString("template");
    }

    public void setTemplate(String template) {
        put("template", template);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public Type getType() {
        return Type.valueOf(getString("type"));
    }

    public void setType(String type) {
        put("type", type);
    }

    @SuppressWarnings("unchecked")
    public List<String> getCategories() {
        return get("categories", List.class);
    }

    public void setCategories(List<String> categories) {
        put("categories", categories);
    }

    public String getKeyword() {
        return getString("keyword");
    }

    public void setKeyword(String keyword) {
        put("keyword", keyword);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    @SuppressWarnings("unchecked")
    public List<String> getTags() {
        return get("tags", List.class);
    }

    public void setTags(List<String> tags) {
        put("tags", tags);
    }

    public String getLanguage() {
        return getString("langugage");
    }

    public void setLanguage(String language) {
        put("language", language);
    }

    public String getUpdateFrequency() {
        return getString("updateFrequency");
    }

    public void setUpdateFrequency(String updateFrequency) {
        put("updateFrequency", updateFrequency);
    }

    public String getPriority() {
        return getString("priority");
    }

    public void setPriority(String priority) {
        put("priority", priority);
    }

    public Date getCreateTime() {
        return getDate("createTime");
    }

    public void setCreateTime(Date createTime) {
        put("createTime", createTime);
    }

    public Date getLastUpdateTime() {
        return getDate("lastUpdateTime");
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        put("lastUpdateTime", lastUpdateTime);
    }

    public int getStatus() {
        return getInteger("status");
    }

    public void setStatus(int status) {
        put("status", status);
    }

    public String getAuthor() {
        return getString("author");
    }

    public void setAuthor(String author) {
        put("author", author);
    }

    public Date getPublishTime() {
        return getDate("publishTime");
    }

    public void setPublishTime(Date publishTime) {
        put("publishTime", publishTime);
    }

    public String getPublisher() {
        return getString("publisher");
    }

    public void setPublisher(String publisher) {
        put("publisher", publisher);
    }

    public String getSubTitle() {
        return getString("subTitle");
    }

    public void setSubTitle(String subTitle) {
        put("subTitle", subTitle);
    }

    public String getImageUrl() {
        return getString("imageUrl");
    }

    public void setImageUrl(String imageUrl) {
        put("imageUrl", imageUrl);
    }

    public boolean isDirectory() {
        return Type.DIRECTORY.equals(getType());
    }

    public enum Type {
        PAGE, DIRECTORY
    }
}
