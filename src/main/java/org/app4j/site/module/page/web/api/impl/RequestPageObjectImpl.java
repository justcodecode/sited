package org.app4j.site.module.page.web.api.impl;

import org.app4j.site.module.page.web.api.RequestPageObject;
import org.app4j.site.web.Request;

/**
 * @author chi
 */
public class RequestPageObjectImpl implements RequestPageObject {
    private final Request request;

    public RequestPageObjectImpl(Request request) {
        this.request = request;
    }

    @Override
    public String path() {
        return request.path();
    }

    @Override
    public String param(String name) {
        return request.query(name).get();
    }

    @Override
    public String cookie(String cookie) {
        return request.cookie(cookie).get();
    }

    @Override
    public String header(String header) {
        return request.header(header).get();
    }

    @Override
    public String url() {
        return request.url();
    }

    @Override
    public String host() {
        return request.host();
    }
}
