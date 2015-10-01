package org.app4j.site.web.impl;

import io.undertow.io.Sender;
import org.app4j.site.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chi
 */
public class BeanBodyResponseHandler implements BodyHandler {
    private final Logger logger = LoggerFactory.getLogger(BeanBodyResponseHandler.class);

    @Override
    public void handle(ResponseImpl response, Sender sender, RequestImpl request) {
        Object bean = ((BeanBody) response.body).bean;
        String responseText = JSON.stringify(bean);
        logger.debug("[response] body={}", responseText);
        sender.send(responseText);
    }
}
