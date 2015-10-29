package org.app4j.site.module.file;


import org.app4j.site.Module;
import org.app4j.site.Site;
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
    public FileModule(Site site) {
        super(site);
    }

    @Override
    public void configure() {
        File dir = property("site.file.dir").isPresent()
            ? new File(property("site.file.dir").get())
            : site.dir("file");

        database().codecs().add(new UploadFileCodec());

        UploadFileService uploadFileService = new UploadFileService(database().get(), new FolderFileRepository(dir));
        bind(UploadFileService.class).to(uploadFileService).export();

        FileController fileController = new FileController(uploadFileService.repository());
        route().get("/f/*", fileController::file)
            .get("/i/*", fileController::image);

        AdminUploadController adminUploadController = new AdminUploadController(uploadFileService);
        AdminUploadFileRESTController adminUploadFileRESTController = new AdminUploadFileRESTController(uploadFileService);

        admin().route()
            .post("/admin/api/file/upload", adminUploadController::clientUpload)
            .post("/admin/file/upload", adminUploadController::upload)
            .get("/admin/api/file/:id", adminUploadFileRESTController::findById)
            .get("/admin/api/file/", adminUploadFileRESTController::findUploadFiles)
            .put("/admin/api/file/:id", adminUploadFileRESTController::updateUploadFile)
            .delete("/admin/api/file/:id", adminUploadFileRESTController::deleteUploadFile);
    }

}
