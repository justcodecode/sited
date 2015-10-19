package org.app4j.site.module.file.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.file.domain.UploadFile;
import org.app4j.site.internal.database.FindView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Optional;

/**
 * @author chi
 */
public class UploadFileService {
    private final FileRepository fileRepository;
    private final MongoCollection<UploadFile> documents;

    public UploadFileService(MongoDatabase db, FileRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.documents = db.getCollection("cms.File", UploadFile.class);
    }

    public FileRepository repository() {
        return fileRepository;
    }

    public UploadFile findByPath(String path) {
        return documents.find(new Document("path", path).append("status", 1)).first();
    }

    public void save(UploadFile uploadFile) {
        if (uploadFile.getCreateTime() == null) {
            uploadFile.setCreateTime(new Date());
        }
        if (uploadFile.getLastUpdateTime() == null) {
            uploadFile.setLastUpdateTime(new Date());
        }
        documents.insertOne(uploadFile);
    }

    public void update(UploadFile uploadFile) {
        documents.replaceOne(new Document("_id", new ObjectId(uploadFile.getId())), uploadFile);
    }

    public FindView<UploadFile> find(int offset, int fetchSize) {
        FindView<UploadFile> uploadFiles = new FindView<>(offset, count());
        documents.find(new Document("status", 1)).sort(new Document("lastUpdateTime", -1)).into(uploadFiles);
        return uploadFiles;
    }

    public long count() {
        return documents.count(new Document("status", 1));
    }

    public Optional<UploadFile> findById(ObjectId objectId) {
        UploadFile uploadFile = documents.find(new Document("_id", objectId)).first();
        if (uploadFile == null) {
            return Optional.empty();
        }
        return Optional.of(uploadFile);
    }

    public void delete(ObjectId objectId) {
        Optional<UploadFile> uploadFileOptional = findById(objectId);
        if (uploadFileOptional.isPresent()) {
            UploadFile uploadFile = uploadFileOptional.get();
            uploadFile.setStatus(0);
            uploadFile.setLastUpdateTime(new Date());

            update(uploadFile);
        }
    }
}
