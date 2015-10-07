package org.app4j.site.runtime.variable.processor;

import org.app4j.site.runtime.template.service.TemplateDialect;
import org.app4j.site.runtime.variable.VariableConfig;
import org.app4j.site.runtime.variable.VariableModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author chi
 */
public class RequireElementTagProcessorTest {
    TemplateEngine templateEngine = new TemplateEngine();


    @Before
    public void setup() {
        VariableConfig variableConfig = new VariableModule();
        variableConfig.add("page", (ref, scope) -> new TestObject("some"));

        TemplateDialect templateDialect = new TemplateDialect();
        templateDialect.add(new RequireElementTagProcessor(templateDialect, variableConfig));

        templateEngine.setDialect(templateDialect);
        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
    }

    @Test
    public void process() {
        String html = templateEngine.process("template/require.html", new Context());
        Assert.assertEquals("\n<span>some</span>", html);
    }

    public class TestObject {
        public final String title;

        public TestObject(String title) {
            this.title = title;
        }
    }
}