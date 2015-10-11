package org.app4j.site.runtime.template.processor;

import com.google.common.base.Strings;
import org.app4j.site.runtime.template.service.TemplateDialect;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

public class TemplateHrefAttrProcessor extends AbstractAttributeTagProcessor implements TemplateProcessorSupport {
    public static final int PRECEDENCE = 1000;
    public static final String ATTRIBUTE_NAME = "href";

    private final String baseUrl;
    private final List<String> baseCdnUrls;

    public TemplateHrefAttrProcessor(TemplateDialect dialect, String baseUrl, List<String> baseCdnUrls) {
        super(dialect, TemplateMode.HTML, dialect.getPrefix(), null, false, ATTRIBUTE_NAME, true, PRECEDENCE, true);
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.baseCdnUrls = baseCdnUrls;
    }


    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        String path = evalAsString(attributeValue, processingContext);

        if (Strings.isNullOrEmpty(path)) {
            path = href(processingContext, tag);
        }

        if (isRelativePath(path)) {
            if (isCdnEnabled(tag)) {
                tag.getAttributes().setAttribute(ATTRIBUTE_NAME, cdnUrl(baseCdnUrls, path));
                tag.getAttributes().removeAttribute("cdn");
            } else {
                tag.getAttributes().setAttribute(ATTRIBUTE_NAME, baseUrl + path);
            }
        }
        tag.getAttributes().removeAttribute(attributeName);
    }

    String href(ITemplateProcessingContext processingContext, IProcessableElementTag tag) {
        String src = tag.getAttributes().getValue(ATTRIBUTE_NAME);
        if (isRelativePath(src)) {
            return template(processingContext).resolve(src).toString();
        }
        return src;
    }
}
