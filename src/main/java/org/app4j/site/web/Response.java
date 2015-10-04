package org.app4j.site.web;

import com.google.common.base.Preconditions;
import com.google.common.net.MediaType;
import org.app4j.site.web.impl.BeanBody;
import org.app4j.site.web.impl.ByteArrayBody;
import org.app4j.site.web.impl.FileBody;
import org.app4j.site.web.impl.ResponseImpl;
import org.app4j.site.web.impl.TemplateBody;
import org.app4j.site.web.impl.TextBody;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author chi
 */
public interface Response {
    static Response text(String text, int statusCode, String contentType) {
        return new ResponseImpl(new TextBody(text))
                .setContentType(contentType)
                .setStatusCode(statusCode);
    }

    static Response text(String text, String contentType) {
        return text(text, 200, contentType);
    }

    static Response bean(Object bean) {
        return bean(bean, 200);
    }

    static Response bean(Object bean, int statusCode) {
        return new ResponseImpl(new BeanBody(bean))
                .setContentType(MediaType.JSON_UTF_8.toString())
                .setStatusCode(statusCode);
    }

    static Response template(String templatePath, Map<String, Object> model) {
        return new ResponseImpl(new TemplateBody(templatePath, model))
                .setContentType("text/html")
                .setStatusCode(200);
    }

    static Response empty() {
        return new ResponseImpl(new TextBody(""))
                .setStatusCode(200);
    }

    static Response bytes(byte[] bytes) {
        return bytes(bytes, MediaType.OCTET_STREAM.toString());
    }

    static Response bytes(byte[] bytes, String contentType) {
        return new ResponseImpl(new ByteArrayBody(bytes))
                .setContentType(contentType)
                .setStatusCode(200);
    }

    static Response file(File file) {
        return new ResponseImpl(new FileBody(file))
                .setStatusCode(200);
    }

    static Response redirect(String url) {
        return new ResponseImpl(new TextBody(""))
                .setHeader("location", url)
                .setStatusCode(302);
    }

    static Response redirect(String url, int statusCode) {
        Preconditions.checkState(statusCode == 301 || statusCode == 302, "invalid redirect status code %s", statusCode);
        return new ResponseImpl(new TextBody(""))
                .setHeader("location", url)
                .setStatusCode(statusCode);
    }

    Response setHeader(String name, String value);

    Response setCookie(String name, String value, int maxAge, boolean secure);

    Response setCookie(String name, String value, int maxAge);

    Response setCookie(String name, String value);

    Response removeCookie(String name);

    Response setContentType(String contentType);

    Response setCharset(Charset charset);

    Response setStatusCode(int statusCode);

}
