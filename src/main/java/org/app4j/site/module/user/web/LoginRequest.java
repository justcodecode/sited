package org.app4j.site.module.user.web;

/**
 * @author chi
 */
public class LoginRequest {
    private String username;
    private String password;
    private String toURL;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToURL() {
        return toURL;
    }

    public void setToURL(String toURL) {
        this.toURL = toURL;
    }
}
