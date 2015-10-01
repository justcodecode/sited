package org.app4j.site.module.question.domain;

import java.util.Date;
import java.util.List;

/**
 * @author chi
 */
public class Answer {
    private String id;
    private String questionId;
    private String userId;
    private String content;

    private int totalUpVotes;
    private int totalDownVotes;
    private boolean accepted;

    private List<String> mentionedUsers;
    private Date createTime;
    private Date lastUpdateTime;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getMentionedUsers() {
        return mentionedUsers;
    }

    public void setMentionedUsers(List<String> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalUpVotes() {
        return totalUpVotes;
    }

    public void setTotalUpVotes(int totalUpVotes) {
        this.totalUpVotes = totalUpVotes;
    }

    public int getTotalDownVotes() {
        return totalDownVotes;
    }

    public void setTotalDownVotes(int totalDownVotes) {
        this.totalDownVotes = totalDownVotes;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
