package org.app4j.site.internal.template.web;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.undertow.util.MimeMappings;
import org.app4j.site.internal.template.AssetsConfig;
import org.app4j.site.util.Resource;
import org.app4j.site.util.Value;
import org.app4j.site.web.Handler;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author chi
 */
public class AssetsHandler implements Handler {
    private final AssetsConfig assetsConfig;
    private boolean cacheEnabled = false;
    private int expireSeconds = 3600;
    private boolean hashPathEnabled = false;

    public AssetsHandler(AssetsConfig assetsConfig) {
        this.assetsConfig = assetsConfig;
    }

    public AssetsHandler enableCache() {
        cacheEnabled = false;
        return this;
    }

    public AssetsHandler enableMd5Path() {
        cacheEnabled = true;
        hashPathEnabled = false;
        return this;
    }

    public AssetsHandler cacheExpireAfter(int seconds) {
        expireSeconds = seconds;
        return this;
    }

    @Override
    public Response handle(Request request) throws IOException {
        Optional<Resource> resource = assetsConfig.get(hashPathEnabled ? new Md5Path(request.path()).path() : request.path());
        if (!resource.isPresent()) {
            throw new NotFoundException(request.path());
        }

        if (cacheEnabled) {
            Value<String> etag = request.header("If-None-Match");
            if (etag.isPresent() && resource.get().md5().equals(etag.get())) {
                return Response.empty()
                    .setStatusCode(304);
            }
        }

        Response response = Response.pipe(body(resource.get()))
            .setContentType(contentType(request.path()))
            .setStatusCode(200);

        if (cacheEnabled) {
            response.setHeader("Cache-Control", "max-age=" + expireSeconds);
            response.setHeader("Etag", resource.get().md5());
        }

        return response;
    }

    InputStream body(Resource resource) {
        return resource.openStream();
    }

    String contentType(String path) {
        String contentType = MimeMappings.DEFAULT_MIME_MAPPINGS.get(Files.getFileExtension(path));
        if (Strings.isNullOrEmpty(contentType)) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

}
