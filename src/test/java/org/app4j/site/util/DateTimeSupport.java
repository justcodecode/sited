package org.app4j.site.util;

import com.github.fakemongo.junit.FongoRule;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author chi
 */
public class DateTimeSupport {
    @Rule
    public FongoRule fongoRule = new FongoRule();

    @Test
    public void execute() {
        Document document = new Document();
        document.put("createTime", LocalDateTime.now());

        fongoRule.getDatabase().getCollection("test").insertOne(document);

        Assert.assertNotNull(JSON.stringify(document));
    }
}
