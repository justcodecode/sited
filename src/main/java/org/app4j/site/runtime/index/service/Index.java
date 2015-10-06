package org.app4j.site.runtime.index.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.app4j.site.runtime.database.DomainCodec;
import org.app4j.site.runtime.database.FindView;
import org.app4j.site.util.Dirs;
import org.app4j.site.util.JSON;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author chi
 */
public class Index<T> {
    private final Logger logger = LoggerFactory.getLogger(Index.class);
    private final File dir;
    private final IndexWriter indexWriter;
    private final IndexLoader<T> indexLoader;
    private final Directory directory;
    private final Analyzer analyzer;
    private final DomainCodec<T> codec;
    private volatile IndexSearcher indexSearcher;

    public Index(File dir, IndexLoader<T> indexLoader, Analyzer analyzer, DomainCodec<T> codec) {
        this.dir = dir;
        this.indexLoader = indexLoader;
        this.analyzer = analyzer;
        this.codec = codec;
        try {
            Dirs.createIfNoneExists(dir);

            directory = FSDirectory.open(dir.toPath());
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            indexWriter = new IndexWriter(directory, config);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public boolean isEmpty() {
        return dir.listFiles() == null;
    }

    public void stop() {
        try {
            indexWriter.close();
            directory.close();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public void clear() {
        try {
            indexWriter.deleteAll();
            indexSearcher = null;
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public IndexLoader<T> loader() {
        return indexLoader;
    }

    public void rebuild() {
        clear();

        for (T object : loader()) {
            index(object);
        }
    }

    IndexSearcher indexSearcher() {
        if (indexSearcher == null) {
            try {
                indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return indexSearcher;
    }

    public Index<T> index(T object) {
        Document document = codec.to(object);

        try {
            if (isIndexExists(document)) {
                logger.debug("update index {} -> {}", dir.getName(), document.getObjectId("_id").toHexString());
                indexWriter.updateDocument(new Term("_id", document.getObjectId("_id").toHexString()), parse(document));
            } else {
                logger.debug("create index {} -> {}", dir.getName(), document.getObjectId("_id").toHexString());
                indexWriter.addDocument(parse(document));
            }
            indexSearcher = null;
        } catch (IOException e) {
            throw new Error(e);
        }
        return this;
    }

    boolean isIndexExists(Document document) {
        if (indexSearcher == null) {
            return false;
        }
        try {
            TopDocs topDocs = indexSearcher().search(new TermQuery(new Term("_id", document.getObjectId("_id").toHexString())), 1);
            return topDocs.totalHits > 0;
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    org.apache.lucene.document.Document parse(Document document) {
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
        doc.add(new StringField("_id", document.getObjectId("_id").toHexString(), Field.Store.YES));
        doc.add(new StringField("body", JSON.stringify(document), Field.Store.YES));
        return doc;
    }

    public FindView<T> search(String keyword, int offset, int fetchSize) {
        try {
            return search(new QueryParser("body", analyzer).parse(keyword), offset, fetchSize);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public FindView<T> search(Query query, int offset, int fetchSize) {
        try {
            TopDocs topDocs = indexSearcher.search(query, (offset + 1) * fetchSize);
            FindView<T> results = new FindView<>(offset, topDocs.totalHits);
            for (int i = offset; i < offset + fetchSize; i++) {
                Document document = JSON.parse(indexSearcher().doc(topDocs.scoreDocs[i].doc).get("body"), Document.class);
                results.add(codec.from(document));
            }
            return results;
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
