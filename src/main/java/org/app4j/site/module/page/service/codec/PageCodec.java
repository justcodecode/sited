package org.app4j.site.module.page.service.codec;

import org.app4j.site.module.page.domain.Page;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;

/**
 * @author chi
 */
public class PageCodec implements Codec<Page> {
    DocumentCodec documentCodec = new DocumentCodec();

    @Override
    public Page decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        Page page = new Page();
        page.putAll(document);
        return page;
    }

    @Override
    public void encode(BsonWriter writer, Page page, EncoderContext encoderContext) {
        Document document = new Document();
        document.putAll(page);
        documentCodec.encode(writer, document, encoderContext);
    }

    @Override
    public Class<Page> getEncoderClass() {
        return Page.class;
    }
}
