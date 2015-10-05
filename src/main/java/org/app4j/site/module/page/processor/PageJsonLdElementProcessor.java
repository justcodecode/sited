package org.app4j.site.module.page.processor;

import com.google.common.collect.Maps;
import org.app4j.site.module.page.variable.PageObject;
import org.app4j.site.runtime.template.TemplateDialect;
import org.app4j.site.runtime.template.processor.TemplateProcessorSupport;
import org.app4j.site.util.JSON;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author chi
 */
public class PageJsonLdElementProcessor extends AbstractElementTagProcessor implements TemplateProcessorSupport {
    public static final int PRECEDENCE = 1000;
    public static final String ELEMENT_NAME = "json-ld";

    private final String baseUrl;

    public PageJsonLdElementProcessor(TemplateDialect dialect, String baseUrl) {
        super(dialect, TemplateMode.HTML, dialect.getPrefix(), ELEMENT_NAME, true, null, false, PRECEDENCE);
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }


    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, String tagTemplateName, int tagLine, int tagCol, IElementTagStructureHandler structureHandler) {
        PageObject pageObject = (PageObject) eval(tag.getAttributes().getValue("page"), processingContext);
        structureHandler.replaceWith("<script type=\"application/ld+json\">" + jsonLd(pageObject) + "</script>", false);
    }

    String jsonLd(PageObject page) {
        Map<String, Object> json = Maps.newHashMap();

        json.put("@context", "http://schema.org");
        json.put("@type", "Article");
        json.put("url", baseUrl + page.get("path"));

        if (page.containsKey("publisher")) {
            json.put("publisher", page.get("publisher"));
        }

        if (page.containsKey("author")) {
            json.put("author", page.get("author"));
        }

        if (page.containsKey("title")) {
            json.put("headline", page.get("title"));
        }

        if (page.containsKey("datePublished")) {
            json.put("datePublished", new SimpleDateFormat("yyyy-MM-dd").format(page.get("datePublished")));
        } else {
            json.put("datePublished", new SimpleDateFormat("yyyy-MM-dd").format(page.get("createTime")));
        }

        if (page.containsKey("lastUpdateTime")) {
            json.put("dateModified", new SimpleDateFormat("yyyy-MM-dd").format(page.get("lastUpdateTime")));
        }

        if (page.containsKey("description")) {
            json.put("description", page.get("description"));
        }

        if (page.containsKey("imageUrl")) {
            json.put("image", baseUrl + page.get("imageUrl"));
        }

        return JSON.stringify(json);
    }
}
