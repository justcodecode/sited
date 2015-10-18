package org.app4j.site.module.page.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.page.Page;
import org.app4j.site.runtime.database.Dumper;
import org.app4j.site.runtime.database.FindView;
import org.app4j.site.runtime.database.MongoCollectionDumper;
import org.app4j.site.web.exception.NotFoundException;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class PageService {
    private final MongoCollection<Page> documents;

    public PageService(MongoDatabase db) {
        this.documents = db.getCollection("site.Page", Page.class);
        if (documents.listIndexes().into(Lists.newArrayList()).size() < 2) {
            documents.createIndex(new Document("lastUpdateTime", -1).append("title", 1));
        }
    }

    public Dumper<Page> dumper() {
        return new MongoCollectionDumper<>(documents);
    }

    public Page root() {
        return findByPath("/").get();
    }

    public FindView<Page> find(int offset, int fetchSize) {
        FindView<Page> results = new FindView<>(offset, documents.count());
        return documents.find(new Document("status", 1)).sort(new Document("lastUpdateTime", -1)).skip(offset).limit(fetchSize).into(results);
    }

    public Optional<Page> findByPath(String path) {
        Page page = documents.find(new Document("path", path).append("status", 1)).first();
        return page == null ? Optional.<Page>empty() : Optional.of(page);
    }

    public FindView<Page> findByCategory(String category, int offset, int fetchSize, String type) {
        FindView<Page> results = new FindView<>(offset, countByCategory(category));
        return documents.find(new Document("categories", category).append("type", type))
            .skip(offset)
            .limit(fetchSize)
            .into(results);
    }


    public Page findByKeyword(String keyword) {
        return documents.find(new Document("keyword", keyword).append("status", 1)).first();
    }

    public long countByCategory(String category) {
        return documents.count(new Document("categories", category).append("status", 1));
    }

    public long totalPages() {
        return documents.count(new Document("status", 1));
    }

    public void saveOrUpdate(Page page) {
        Optional<Page> oldPageOptional = findByPath(page.path());

        if (oldPageOptional.isPresent()) {
            page.setId(oldPageOptional.get().id());
            page.setCreateTime(oldPageOptional.get().createTime());
        } else {
            page.setCreateTime(new Date());
        }

        String path = page.path();
        Preconditions.checkState(path.startsWith("/"), "path %s must start with /", path);

        page.setCategories(parseCategories(path));
        page.setStatus(1);
        page.setLastUpdateTime(new Date());

        if (oldPageOptional.isPresent()) {
            documents.replaceOne(new Document("_id", new ObjectId(page.id())), page);
        } else {
            documents.insertOne(page);
        }
    }

    private List<String> parseCategories(String path) {
        List<String> categories = Lists.newArrayList("/");

        for (int i = 1; i < path.length(); i++) {
            char c = path.charAt(i);

            if (c == '/') {
                categories.add(path.substring(0, i + 1));
            }
        }

        return categories;
    }

    public Page findById(ObjectId objectId) {
        return documents.find(new Document("_id", objectId)).first();
    }

    public void deletePage(ObjectId objectId) {
        Page page = findById(objectId);

        if (page == null) {
            throw new NotFoundException("page not found");
        }

        page.setStatus(0);
        page.setLastUpdateTime(new Date());

        documents.replaceOne(new Document("_id", objectId), page);
    }
}


