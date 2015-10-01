package org.app4j.site.web.impl;

import io.undertow.io.Sender;

/**
 * @author neo
 */
public class TextBodyResponseHandler implements BodyHandler {
    @Override
    public void handle(ResponseImpl response, Sender sender, RequestImpl request) {
        TextBody body = (TextBody) response.body;
        sender.send(body.text);
    }
}
