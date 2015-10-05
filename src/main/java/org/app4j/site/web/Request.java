package org.app4j.site.web;

import org.app4j.site.Scope;
import org.app4j.site.util.Value;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author chi
 */
public interface Request extends Scope {
    String url();

    Method method();

    String host();

    int port();

    String schema();

    String accept();

    String path();

    Parameter<String> path(String name);

    <T> Parameter<T> path(String name, Class<T> type);

    Parameter<String> query(String key);

    <T> Parameter<T> query(String key, Class<T> type);

    <T> T body(Class<T> type) throws IOException;

    Map<String, String> parameters();

    Value<String> header(String name);

    <T> Value<T> header(String name, Class<T> type);

    Map<String, String> headers();

    Value<String> cookie(String name);

    <T> Value<T> cookie(String name, Class<T> type);

    Map<String, String> cookies();

    String contentType();

    Charset charset();

    Locale locale();

    Date timestamp();

    enum Method {
        GET, POST, PUT, DELETE
    }
}
