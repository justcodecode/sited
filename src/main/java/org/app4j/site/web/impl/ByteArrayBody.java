package org.app4j.site.web.impl;

/**
 * @author chi
 */
public class ByteArrayBody implements Body {
    final byte[] bytes;

    public ByteArrayBody(byte[] bytes) {
        this.bytes = bytes;
    }
}
