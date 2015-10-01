package org.app4j.site.runtime.template.processor;//package io.jcms.site.runtime.template.processor;
//
//import com.google.common.collect.Lists;
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
//public class TemplateHrefAttrProcessorTest {
//    TemplateEngine templateEngine;
//
//    @Before
//    public void setup() {
//        templateEngine = new TemplateEngine();
//
//        TemplateDialect dialect = new TemplateDialect();
//        dialect.add(new TemplateHrefAttrProcessor("http://localhost:8080", Lists.newArrayList("http://127.0.0.1")));
//        templateEngine.setDialect(dialect);
//        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
//    }
//
//    @Test
//    public void process() {
//        Context context = new Context();
//        context.setVariable("path", "/1.html");
//        String result = templateEngine.process("template/href.html", context);
//        Assert.assertEquals("<a href=\"http://localhost:8080/1.html?some=1\">test</a>", result);
//    }
//
//    @Test
//    public void cdn() {
//        String result = templateEngine.process("template/href.cdn.html", new Context());
//        Assert.assertEquals("<link rel=\"stylesheet\" href=\"http://127.0.0.1/some.css\"/>", result);
//    }
//}