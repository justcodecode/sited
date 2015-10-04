package org.app4j.site.web.impl;

import com.google.common.base.Charsets;
import org.app4j.site.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author chi
 */
public class BeanBodyResponseHandler implements BodyHandler {
    private final Logger logger = LoggerFactory.getLogger(BeanBodyResponseHandler.class);

    @Override
    public InputStream handle(ResponseImpl response) {
        Object bean = ((BeanBody) response.body).bean;
        String responseText = JSON.stringify(bean);
        logger.debug("[response] body={}", responseText);
        return new ByteArrayInputStream(responseText.getBytes(Charsets.UTF_8));
    }
}
