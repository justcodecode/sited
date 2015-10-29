package org.app4j.site.module.file.web.admin;

import org.app4j.site.module.file.domain.UploadFile;
import org.app4j.site.module.file.service.UploadFileService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author chi
 */
public class AdminUploadController {
    private final UploadFileService uploadFileService;

    public AdminUploadController(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    public Response upload(Request request) throws IOException {
        File file = request.body(File.class);
        String fileName = request.query("fileName").orElse(file.getName()).get();
        try (InputStream in = new FileInputStream(file)) {
            String path = uploadFileService.repository().put(in, fileName);

            UploadFile uploadFile = new UploadFile();
            uploadFile.setPath("/f/" + path);
            uploadFile.setFileName(file.getName());
            uploadFile.setFileName(fileName);
            uploadFile.setCreateTime(new Date());
            uploadFile.setLastUpdateTime(new Date());
            uploadFile.setStatus(1);
            uploadFileService.save(uploadFile);

            return Response.bean(uploadFile);
        }
    }

    public Response clientUpload(Request request) throws IOException {
        File file = request.body(File.class);
        try (InputStream in = new FileInputStream(file)) {
            ClientUploadResponse clientUploadResponse = new ClientUploadResponse();
            clientUploadResponse.path = uploadFileService.repository().put(in, file.getName());
            return Response.bean(clientUploadResponse);
        }
    }
}
