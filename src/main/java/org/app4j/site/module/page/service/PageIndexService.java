package org.app4j.site.module.page.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.app4j.site.module.page.Page;
import org.app4j.site.internal.database.FindView;
import org.app4j.site.internal.index.service.Index;

/**
 * @author chi
 */
public class PageIndexService {
    private final Index<Page> index;
    private final Analyzer analyzer = new StandardAnalyzer();

    public PageIndexService(Index<Page> index) {
        this.index = index;
    }

    public FindView<Page> relatedPages(Page page, int fetchSize) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        try {
            Query query = new QueryParser("body", analyzer).parse(page.title());
            builder.add(query, BooleanClause.Occur.MUST);
            return index.search(builder.build(), 0, fetchSize);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public void rebuild() {
        index.rebuild();
    }
}
