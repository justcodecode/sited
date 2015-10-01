package org.app4j.site.web.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.net.MediaType;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.util.HeaderValues;
import org.app4j.site.Binding;
import org.app4j.site.Scope;
import org.app4j.site.Site;
import org.app4j.site.util.JSON;
import org.app4j.site.util.Value;
import org.app4j.site.web.Parameter;
import org.app4j.site.web.Request;

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
class RequestImpl implements Request {
    private final HttpServerExchange exchange;
    private final Date timestamp;
    private final Map<String, String> parameters = Maps.newHashMap();
    private final Scope parentScope;
    private final Map<Binding.Key<?>, Binding<?>> bindings = Maps.newHashMap();
    private final Locale locale;
    private final Charset charset;

    RequestImpl(HttpServerExchange exchange, Scope parentScope) {
        this.exchange = exchange;
        this.parentScope = parentScope;

        Binding.Key<Request> key = new Binding.Key<>(Request.class);
        bindings.put(key, new Binding<>(key, (key1, scope) -> this, Site.class));

        timestamp = new Date();
        charset = parentScope.require(Site.class).charset();
        locale = parentScope.require(Site.class).locale();

        for (Map.Entry<String, Deque<String>> entry : exchange.getQueryParameters().entrySet()) {
            parameters.put(entry.getKey(), entry.getValue().peek());
        }

        if ("POST".equals(method()) && "application/x-www-form-urlencoded".equals(contentType())) {
            FormParserFactory formParserFactory = FormParserFactory.builder().build();
            try {
                FormDataParser parser = formParserFactory.createParser(exchange);
                parser.setCharacterEncoding(charset.name());
                FormData formData = parser.parseBlocking();
                for (String name : formData) {
                    FormData.FormValue formValue = formData.get(name).getFirst();
                    parameters.put(name, formValue.getValue());
                }
            } catch (IOException e) {
                throw new Error(e);
            }
        }
    }

    public <T> Binding.To<T> bind(Class<T> type) {
        Binding.Key<T> key = new Binding.Key<>(type);
        return provider -> {
            bindings.put(key, new Binding<>(key, provider, Site.class));
            return () -> {
                throw new Error("export not support for request scope binding");
            };
        };
    }

    @Override
    public <T> T require(Class<T> type) {
        return require(type, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T require(Class<T> type, String qualifier) {
        Binding.Key key = new Binding.Key(type, qualifier);
        if (bindings.containsKey(key)) {
            return (T) bindings.get(key).provider.get(key, this);
        }
        return parentScope.require(type);
    }

    @Override
    public String url() {
        return exchange.getRequestURL();
    }

    @Override
    public Method method() {
        return null;
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
    public String path() {
        return exchange.getRequestPath();
    }

    @Override
    public Parameter<String> path(String key) {
        return null;
    }

    @Override
    public <T> Parameter<T> path(String key, Class<T> type) {
        return null;
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
        try (InputStream in = exchange.getInputStream()) {
            if (String.class.isAssignableFrom(type)) {
                return (T) CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
            } else if (byte[].class.isAssignableFrom(type)) {
                return (T) ByteStreams.toByteArray(in);
            } else {
                return JSON.parse(CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8)), type);
            }
        }
    }

    @Override
    public InputStream body() throws IOException {
        return null;
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