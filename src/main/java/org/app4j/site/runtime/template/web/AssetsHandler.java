package org.app4j.site.runtime.template.web;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.undertow.util.MimeMappings;
import org.app4j.site.runtime.template.AssetsConfig;
import org.app4j.site.runtime.template.Resource;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.IOException;

/**
 * @author chi
 */
public class AssetsHandler implements Handler {
    private final AssetsConfig assetsConfig;

    public AssetsHandler(AssetsConfig assetsConfig) {
        this.assetsConfig = assetsConfig;
    }

    @Override
    public Response handle(Request request) throws IOException {
        Resource resource = assetsConfig.get(new Md5Path(request.path()).path());

        String contentType = MimeMappings.DEFAULT_MIME_MAPPINGS.get(Files.getFileExtension(request.path()));
        if (Strings.isNullOrEmpty(contentType)) {
            contentType = "application/octet-stream";
        }

        return Response.bytes(resource.content())
                .setContentType(contentType)
                .setStatusCode(200);
    }
}
