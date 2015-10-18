package org.app4j.site.web.impl;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import org.app4j.site.Site;
import org.app4j.site.util.JSON;
import org.thymeleaf.context.Context;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.function.Function;

/**
 * @author chi
 */
public class SiteHandler implements HttpHandler {
    private final Map<Class, Function<? extends Body, InputStream>> handlers = Maps.newHashMap();
    private final Site site;

    public SiteHandler(Site site) {
        this.site = site;

        handlers.put(BeanBody.class, new Function<BeanBody, InputStream>() {
            @Override
            public InputStream apply(BeanBody body) {
                Object bean = body.bean;
                return new ByteArrayInputStream(JSON.stringify(bean).getBytes(Charsets.UTF_8));
            }
        });
        handlers.put(TextBody.class, new Function<TextBody, InputStream>() {
            @Override
            public InputStream apply(TextBody body) {
                return new ByteArrayInputStream(body.text.getBytes(Charsets.UTF_8));
            }
        });
        handlers.put(ByteArrayBody.class, new Function<ByteArrayBody, InputStream>() {
            @Override
            public InputStream apply(ByteArrayBody body) {
                return new ByteArrayInputStream(body.bytes);
            }
        });
        handlers.put(FileBody.class, new Function<FileBody, InputStream>() {
            @Override
            public InputStream apply(FileBody body) {
                try {
                    return new FileInputStream(body.file);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });
        handlers.put(TemplateBody.class, new Function<TemplateBody, InputStream>() {
            @Override
            public InputStream apply(TemplateBody body) {
                Context context = new Context();
                context.setVariables(body.model);
                String html = site.template().engine().process(body.templatePath, context);
                return new ByteArrayInputStream(html.getBytes(Charsets.UTF_8));
            }
        });
        handlers.put(InputStreamBody.class, new Function<InputStreamBody, InputStream>() {
            @Override
            public InputStream apply(InputStreamBody body) {
                return body.inputStream;
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        RequestImpl request = new RequestImpl(exchange, site);
        try {
            ResponseImpl response = (ResponseImpl) site.handle(request);
            write(exchange, response);
        } catch (Throwable e) {
            ResponseImpl response = (ResponseImpl) site.handleError(request, e);
            write(exchange, response);
        } finally {
            exchange.endExchange();
        }
    }

    @SuppressWarnings("unchecked")
    <T extends Body> void write(HttpServerExchange exchange, ResponseImpl response) throws IOException {
        Function<T, InputStream> handler = (Function<T, InputStream>) handlers.get(response.body.getClass());
        if (handler == null)
            throw new Error(String.format("unexpected body class, body=%s", response.body.getClass().getCanonicalName()));

        try (InputStream inputStream = handler.apply((T) response.body)) {
            exchange.setStatusCode(response.statusCode);
            response.cookies.forEach(cookie -> exchange.getResponseCookies().put(cookie.getName(), cookie));
            response.headers.forEach((name, value) -> exchange.getResponseHeaders().put(new HttpString(name), value));
            if (!exchange.isBlocking()) {
                exchange.startBlocking();
            }
            ByteStreams.copy(inputStream, exchange.getOutputStream());
        }
    }
}