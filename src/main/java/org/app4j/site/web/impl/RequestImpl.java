package org.app4j.site.web.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.google.common.net.MediaType;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.HeaderValues;
import org.app4j.site.Binding;
import org.app4j.site.ScopeImpl;
import org.app4j.site.Site;
import org.app4j.site.util.JSON;
import org.app4j.site.util.Value;
import org.app4j.site.web.Parameter;
import org.app4j.site.web.Request;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author chi
 */
public class RequestImpl extends ScopeImpl implements Request {
    private final HttpServerExchange exchange;
    private final Date timestamp;
    private final Map<String, String> parameters = Maps.newHashMap();
    private final Locale locale;
    private final Charset charset;
    private final Method method;

    RequestImpl(HttpServerExchange exchange, ScopeImpl parent) {
        super(parent);

        bind(Request.class).to(this);
        this.exchange = exchange;
        this.method = Method.valueOf(exchange.getRequestMethod().toString());

        timestamp = new Date();
        charset = parent.require(Site.class).charset();
        locale = parent.require(Site.class).locale();

        for (Map.Entry<String, Deque<String>> entry : exchange.getQueryParameters().entrySet()) {
            parameters.put(entry.getKey(), entry.getValue().peek());
        }
    }

    @Override
    public final <T> Binding.Named<T> bind(Class<T> type) {
        return super.bind(type);
    }

    @Override
    public String url() {
        return exchange.getRequestURL();
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public String host() {
        return exchange.getHostName();
    }

    @Override
    public int port() {
        return exchange.getHostPort();
    }

    @Override
    public String schema() {
        return exchange.getRequestScheme();
    }

    @Override
    public String accept() {
        return header("accept").orElse("").get();
    }

    @Override
    public String path() {
        return exchange.getRequestPath();
    }

    @Override
    public Parameter<String> path(String name) {
        return query(name);
    }

    @Override
    public <T> Parameter<T> path(String name, Class<T> type) {
        return query(name, type);
    }

    @Override
    public Parameter<String> query(String name) {
        return new Parameter<>(name, parameters.get(name));
    }

    @Override
    public <T> Parameter<T> query(String name, Class<T> type) {
        if (parameters.containsKey(name)) {
            return new Parameter<>(name, JSON.mapper().convertValue(parameters.get(name), type));
        } else {
            return new Parameter<>(name, null);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T body(Class<T> type) throws IOException {
        if (method == Method.GET || method == Method.DELETE) {
            throw new Error(String.format("%s request has no body", method));
        }

        String contentType = contentType();

        if ("application/json".equalsIgnoreCase(contentType)) {
            exchange.startBlocking();
            try (InputStream in = exchange.getInputStream()) {
                return JSON.parse(CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8)), type);
            }
        } else if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
            exchange.startBlocking();
            FormParserFactory formParserFactory = FormParserFactory.builder().build();
            FormDataParser parser = formParserFactory.createParser(exchange);
            parser.setCharacterEncoding(charset.name());
            FormData formData = parser.parseBlocking();

            Map<String, String> form = Maps.newHashMap();
            for (String name : formData) {
                FormData.FormValue formValue = formData.get(name).getFirst();
                form.put(name, formValue.getValue());
            }
            return JSON.mapper().convertValue(form, type);
        } else if ("multipart/form-data".equalsIgnoreCase(contentType) && type.equals(File.class)) {
            exchange.startBlocking();
            FormParserFactory formParserFactory = FormParserFactory.builder().build();
            FormDataParser parser = formParserFactory.createParser(exchange);
            parser.setCharacterEncoding(charset.name());
            FormData formData = parser.parseBlocking();
            for (String name : formData) {
                FormData.FormValue formValue = formData.get(name).getFirst();
                if (formValue.isFile()) {
                    parameters.put("fileName", formValue.getFileName());
                    return (T) formValue.getPath().toFile();
                }
            }
        }

        throw new Error(String.format("invalid content type %s", contentType));
    }

    @Override
    public Map<String, String> parameters() {
        return parameters;
    }

    @Override
    public Value<String> header(String name) {
        return header(name, String.class);
    }

    @Override
    public <T> Value<T> header(String name, Class<T> type) {
        if (exchange.getRequestHeaders().contains(name)) {
            String str = exchange.getRequestHeaders().get(name).getFirst();
            T value = JSON.mapper().convertValue(str, type);
            return new Value<T>(name, value);
        } else {
            return new Value<T>(name, null) {
                @Override
                public T get() {
                    Preconditions.checkNotNull(value, "missing header %s", name);
                    return super.get();
                }
            };
        }
    }

    @Override
    public Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        for (HeaderValues headerValues : exchange.getRequestHeaders()) {
            headers.put(headerValues.getHeaderName().toString(), headerValues.getFirst());
        }
        return headers;
    }

    @Override
    public Value<String> cookie(String name) {
        return cookie(name, String.class);
    }

    @Override
    public <T> Value<T> cookie(String name, Class<T> type) {
        if (exchange.getRequestCookies().containsKey(name)) {
            String str = exchange.getRequestCookies().get(name).getValue();
            return new Value<T>(name, JSON.mapper().convertValue(str, type));
        } else {
            return new Value<T>(name, null) {
                @Override
                public T get() {
                    Preconditions.checkNotNull(value, "missing cookie %s", name);
                    return super.get();
                }
            };
        }
    }

    @Override
    public Map<String, String> cookies() {
        Map<String, String> cookies = Maps.newHashMap();
        for (Map.Entry<String, Cookie> cookieEntry : exchange.getRequestCookies().entrySet()) {
            cookies.put(cookieEntry.getKey(), cookieEntry.getValue().getValue());
        }
        return cookies;
    }

    @Override
    public final String contentType() {
        Value<String> contentTypeHeader = header("content-type");
        if (contentTypeHeader.isPresent()) {
            MediaType mediaType = MediaType.parse(contentTypeHeader.get());
            return mediaType.type() + '/' + mediaType.subtype();
        }
        return null;
    }


    @Override
    public Charset charset() {
        return charset;
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public Date timestamp() {
        return timestamp;
    }
}