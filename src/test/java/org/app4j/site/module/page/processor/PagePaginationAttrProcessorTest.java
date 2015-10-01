package org.app4j.site.module.page.processor;//package io.jcms.site.module.page.processor;
//
//import io.jcms.site.runtime.database.FindView;
//import io.jcms.site.runtime.template.TemplateDialect;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//
///**
// * @author chi
// */
//public class PagePaginationAttrProcessorTest {
//    TemplateEngine templateEngine;
//    FindView<Object> findView;
//
//    @Before
//    public void setup() {
//        templateEngine = new TemplateEngine();
//
//        TemplateDialect dialect = new TemplateDialect();
//        dialect.add(new PagePaginationAttrProcessor("http://localhost:8080"));
//        templateEngine.setDialect(dialect);
//        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
//
//        findView = new FindView<>(0, 20);
//    }
//
//    @Test
//    public void process() {
//        Context context = new Context();
//        context.setVariable("findView", findView);
//        String result = templateEngine.process("template/pagination.html", context);
//        Assert.assertEquals("<ul><li class=\"active\"><a href=\"http://localhost:8080/some/1/\">1</a></li><li><a href=\"http://localhost:8080/some/2/\">2</a></li></ul>", result);
//    }
//}