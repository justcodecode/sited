package org.app4j.site.runtime.template.web;

import org.app4j.site.runtime.template.Assets;
import org.app4j.site.runtime.template.Resource;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class AssetsHandler implements Handler {
    private final Assets assets;

    public AssetsHandler(Assets assets) {
        this.assets = assets;
    }

    @Override
    public Response handle(Request request) throws IOException {
        String path = request.path();
        Md5Path md5Path = new Md5Path(path);
        Resource resource = assets.get(md5Path.path());
        return Response.bytes(resource.content());
    }
}
