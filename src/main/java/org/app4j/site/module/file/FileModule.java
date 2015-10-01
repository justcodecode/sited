package org.app4j.site.module.file;


import org.app4j.site.Module;
import org.app4j.site.module.file.service.FolderFileRepository;
import org.app4j.site.module.file.service.UploadFileService;
import org.app4j.site.module.file.service.codec.UploadFileCodec;
import org.app4j.site.module.file.web.FileController;
import org.app4j.site.module.file.web.admin.AdminUploadController;
import org.app4j.site.module.file.web.admin.AdminUploadFileRESTController;

import java.io.File;

/**
 * @author chi
 */
public class FileModule extends Module {
    @Override
    public void configure() {
        File fileDir = new File(site().dir(), "file");

        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        database().codecs().add(new UploadFileCodec());

        UploadFileService uploadFileService = new UploadFileService(database().get(), new FolderFileRepository(fileDir));
        bind(UploadFileService.class).to(uploadFileService).export();

        FileController fileController = new FileController(uploadFileService.repository());
        route().get("/f/*", fileController::file)
                .get("/i/*", fileController::image);

        AdminUploadController adminUploadController = new AdminUploadController(uploadFileService);
        admin().post("/admin/file/upload", adminUploadController::upload);

        AdminUploadFileRESTController adminUploadFileRESTController = new AdminUploadFileRESTController(uploadFileService);
        admin().get("/admin/api/file/:id", adminUploadFileRESTController::findById);
        admin().get("/admin/api/file/", adminUploadFileRESTController::findUploadFiles);
        admin().put("/admin/api/file/:id", adminUploadFileRESTController::updateUploadFile);
        admin().delete("/admin/api/file/:id", adminUploadFileRESTController::deleteUploadFile);
    }

    @Override
    protected String name() {
        return "file";
    }
}
