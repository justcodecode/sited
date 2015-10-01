package org.app4j.site.web.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.CookieImpl;
import org.app4j.site.web.Response;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class ResponseImpl implements Response {
    final Body body;
    final Map<String, String> headers = Maps.newHashMap();
    final List<Cookie> cookies = Lists.newArrayList();

    String contentType;
    Charset charset;
    int statusCode = 200;

    public ResponseImpl(Body body) {
        this.body = body;
    }

    @Override
    public Response setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    @Override
    public Response setCookie(String name, String value, int maxAge, boolean secure) {
        Cookie cookie = new CookieImpl(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookies.add(cookie);
        return this;
    }

    @Override
    public Response setCookie(String name, String value, int maxAge) {
        return setCookie(name, value, maxAge, false);
    }

    @Override
    public Response setCookie(String name, String value) {
        return setCookie(name, value, Integer.MAX_VALUE, false);
    }

    @Override
    public Response removeCookie(String name) {
        cookies.removeIf(cookie -> cookie.getName().equals(name));
        return this;
    }

    @Override
    public Response setContentType(String mimeType) {
        this.contentType = mimeType;
        return this;
    }

    @Override
    public Response setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    @Override
    public Response setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}