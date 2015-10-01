package org.app4j.site.web.impl;

import com.google.common.collect.Maps;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.app4j.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author chi
 */
public class SiteHandler implements HttpHandler {
    private final Logger logger = LoggerFactory.getLogger(SiteHandler.class);
    private final Map<Class, BodyHandler> handlers = Maps.newHashMap();
    private final Site site;

    public SiteHandler(Site site) {
        this.site = site;
        handlers.put(BeanBody.class, new BeanBodyResponseHandler());
        handlers.put(TextBody.class, new TextBodyResponseHandler());
        handlers.put(TemplateBody.class, new TemplateBodyResponseHandler(null));
        handlers.put(ByteArrayBody.class, new ByteArrayBodyResponseHandler());
        handlers.put(FileBody.class, new FileBodyResponseHandler());
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        try {
            RequestImpl request = new RequestImpl(exchange, site);
            ResponseImpl response = (ResponseImpl) site.handle(request);

            BodyHandler handler = handlers.get(response.body.getClass());
            if (handler == null)
                throw new Error(String.format("unexpected body class, body=%s", response.body.getClass().getCanonicalName()));
            logger.debug("responseHandlerClass={}", handler.getClass().getCanonicalName());
            handler.handle(response, exchange.getResponseSender(), request);
        } catch (Exception e) {
            throw new Error(e);
        } finally {
            exchange.endExchange();
        }
    }
}