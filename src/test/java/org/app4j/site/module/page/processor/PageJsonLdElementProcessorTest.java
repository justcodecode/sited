package org.app4j.site.module.page.processor;//package io.jcms.site.module.page.processor;
//
//import io.jcms.site.module.page.web.api.DirectoryPageObject;
//import io.jcms.site.module.page.web.api.PagePageObject;
//import io.jcms.site.runtime.database.FindView;
//import io.jcms.site.runtime.template.TemplateDialect;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author chi
// */
//public class PageJsonLdElementProcessorTest {
//    TemplateEngine templateEngine;
//    TestPageObject pageObject;
//
//    @Before
//    public void setup() {
//        templateEngine = new TemplateEngine();
//
//        TemplateDialect dialect = new TemplateDialect();
//        dialect.add(new PageJsonLdElementProcessor("http://localhost:8080"));
//        templateEngine.setDialect(dialect);
//        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
//
//        pageObject = new TestPageObject();
//        pageObject.put("title", "some title");
//        pageObject.put("description", "some description");
//        pageObject.put("createTime", new Date());
//        pageObject.put("lastUpdateTime", new Date());
//        pageObject.put("path", "/1.html");
//        pageObject.put("status", 1);
//    }
//
//    @Test
//    public void process() {
//        Context context = new Context();
//        context.setVariable("page", pageObject);
//        String result = templateEngine.process("template/json-ld.html", context);
//        Assert.assertEquals("<script type=\"application/ld+json\">{\"datePublished\":\"2015-09-20\",\"@type\":\"Article\",\"description\":\"some description\",\"dateModified\":\"2015-09-20\",\"@context\":\"http://schema.org\",\"headline\":\"some title\",\"url\":\"http://localhost:8080/1.html\"}</script>", result);
//    }
//
//
//    class TestPageObject extends HashMap<String, Object> implements PagePageObject {
//        @Override
//        public FindView<PagePageObject> relatedPages(int offset, int fetchSize) {
//            return null;
//        }
//
//        @Override
//        public DirectoryPageObject directory() {
//            return null;
//        }
//
//        @Override
//        public List<DirectoryPageObject> tags() {
//            return null;
//        }
//    }
//}