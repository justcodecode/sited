package org.app4j.site.module.page.service.codec;

import org.app4j.site.module.page.Page;
import org.app4j.site.runtime.database.DomainCodec;
import org.bson.Document;

/**
 * @author chi
 */
public class PageCodec implements DomainCodec<Page> {
    @Override
    public Page from(Document doc) {
        Page page = new Page();
        page.putAll(doc);
        return page;
    }

    @Override
    public Document to(Page object) {
        Document document = new Document();
        document.putAll(object);
        return document;
    }
}
