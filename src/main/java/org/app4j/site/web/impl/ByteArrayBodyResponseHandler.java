package org.app4j.site.web.impl;

import io.undertow.io.Sender;

import java.nio.ByteBuffer;

/**
 * @author chi
 */
public class ByteArrayBodyResponseHandler implements BodyHandler {
    @Override
    public void handle(ResponseImpl response, Sender sender, RequestImpl request) {
        ByteArrayBody body = (ByteArrayBody) response.body;
        sender.send(ByteBuffer.wrap(body.bytes));
    }
}
