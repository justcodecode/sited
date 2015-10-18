package org.app4j.site.module.page.variable;


import org.app4j.site.Scope;

import java.util.Map;

/**
 * @author chi
 */
public class RequestVariable implements Variable<RequestVariable.Request> {
    @Override
    public Request eval(VariableRef variableRef, Scope scope) {
        org.app4j.site.web.Request request = scope.require(org.app4j.site.web.Request.class);

        Request requestObject = new Request();
        requestObject.path = request.path();
        requestObject.cookies = request.cookies();
        requestObject.params = request.parameters();
        requestObject.headers = request.headers();
        requestObject.url = request.url();

        return requestObject;
    }

    public static class Request {
        public String path;
        public String url;
        public Map<String, String> params;
        public Map<String, String> cookies;
        public Map<String, String> headers;
    }
}
