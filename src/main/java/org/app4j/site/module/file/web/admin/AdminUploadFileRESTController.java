package org.app4j.site.module.file.web.admin;

import org.app4j.site.module.file.domain.UploadFile;
import org.app4j.site.module.file.service.UploadFileService;
import org.app4j.site.runtime.route.NotFoundException;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Optional;

/**
 * @author chi
 */
public class AdminUploadFileRESTController {
    private final UploadFileService uploadFileService;

    public AdminUploadFileRESTController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    public Response findById(Request request) {
        String id = request.path(":id").get();
        Optional<UploadFile> uploadFileOptional = uploadFileService.findById(new ObjectId(id));

        if (!uploadFileOptional.isPresent()) {
            throw new NotFoundException(request.path());
        }
        return Response.bean(uploadFileOptional.get());
    }

    public Response updateUploadFile(Request request) throws IOException {
        UploadFile uploadFile = request.body(UploadFile.class);
        uploadFileService.update(uploadFile);
        return Response.empty();
    }

    public Response findUploadFiles(Request request) throws IOException {
        int offset = request.query("offset", Integer.class).orElse(0).get();
        int fetchSize = request.query("fetchSize", Integer.class).orElse(20).get();
        return Response.bean(uploadFileService.find(offset, fetchSize));
    }

    public Response deleteUploadFile(Request request) throws IOException {
        String id = request.path(":id").get();
        uploadFileService.delete(new ObjectId(id));
        return Response.empty();
    }
}
