package org.app4j.site.module.file.web.admin;

import com.google.common.collect.Lists;
import io.undertow.server.handlers.form.FormData;
import org.app4j.site.module.file.domain.UploadFile;
import org.app4j.site.module.file.service.UploadFileService;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Deque;
import java.util.List;

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
        try (InputStream in = new FileInputStream(file)) {
            String path = uploadFileService.repository().put(in, file.getName());

            UploadFile uploadFile = new UploadFile();
            uploadFile.setPath("/f/" + path);
            uploadFile.setTitle(file.getName());
            uploadFile.setTitle(request.query("fileName").orElse(file.getName()).get());
            uploadFile.setCreateTime(new Date());
            uploadFile.setLastUpdateTime(new Date());
            uploadFile.setStatus(1);
            uploadFileService.save(uploadFile);

            return Response.bean(uploadFile);
        }
    }

    String formData(String field, FormData formData) {
        Deque<FormData.FormValue> formValues = formData.get(field);
        if (formValues == null) {
            return null;
        }

        return formValues.peekFirst().getValue();
    }

    List<String> listFormData(String field, FormData formData) {
        List<String> values = Lists.newArrayList();

        Deque<FormData.FormValue> formValues = formData.get(field);
        if (formValues == null) {
            return null;
        }

        for (FormData.FormValue formValue : formValues) {
            values.add(formValue.getValue());
        }
        return values;
    }
}
