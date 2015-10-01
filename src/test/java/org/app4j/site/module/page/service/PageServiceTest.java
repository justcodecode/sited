package org.app4j.site.module.page.service;

import com.github.fakemongo.junit.FongoRule;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoDatabase;
import org.app4j.site.module.page.domain.Page;
import org.app4j.site.module.page.service.codec.PageCodec;
import org.app4j.site.runtime.database.SimpleCodecRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

/**
 * @author chi
 */
public class PageServiceTest {
    @Rule
    public FongoRule fongoRule = new FongoRule();
    PageService pageService;


    @Before
    public void setup() {
        SimpleCodecRegistry simpleCodecRegistry = new SimpleCodecRegistry();
        simpleCodecRegistry.add(new PageCodec());
        MongoDatabase db = fongoRule.getDatabase().withCodecRegistry(simpleCodecRegistry);
        pageService = new PageService(db);

        Page page = new Page();
        page.setPath("/");
        page.setContent("some");
        page.setCreateTime(new Date());

        pageService.saveOrUpdate(page);
    }

    @Test
    public void saveOrUpdate() {
        Page page = new Page();
        page.setPath("/1.html");
        page.setContent("<p><img src='1.jpg'/>some text</p>");
        page.setCreateTime(new Date());

        pageService.saveOrUpdate(page);

        Page savedPage = pageService.findByPath("/1.html").get();
        Assert.assertEquals(Lists.newArrayList("/"), savedPage.getCategories());
        Assert.assertEquals("1.jpg", savedPage.getImageUrl());
    }
}