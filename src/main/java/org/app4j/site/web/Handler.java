package org.app4j.site.web;


/**
 * @author chi
 */
public interface Handler {
    Response handle(Request request) throws Exception;
}
