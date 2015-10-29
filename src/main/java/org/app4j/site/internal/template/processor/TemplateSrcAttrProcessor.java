package org.app4j.site.internal.template.processor;

import com.google.common.base.Strings;
import org.app4j.site.internal.template.service.TemplateDialect;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

public class TemplateSrcAttrProcessor extends AbstractAttributeTagProcessor implements TemplateProcessorSupport {
    public static final int PRECEDENCE = 1000;
    public static final String ATTRIBUTE_NAME = "src";

    private final String baseUrl;
    private final List<String> baseCdnUrls;

    public TemplateSrcAttrProcessor(TemplateDialect dialect, String baseUrl, List<String> baseCdnUrls) {
        super(dialect, TemplateMode.HTML, dialect.getPrefix(), null, false, ATTRIBUTE_NAME, true, PRECEDENCE, true);
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.baseCdnUrls = baseCdnUrls;
    }

    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        tag.getAttributes().removeAttribute(attributeName);

        String path = evalAsString(attributeValue, processingContext);

        if (Strings.isNullOrEmpty(path)) {
            path = src(processingContext, tag);
        }

        if (!isRelativePath(path)) {
            tag.getAttributes().setAttribute(ATTRIBUTE_NAME, path);
            return;
        }

        if (isImageElement(tag)) {
            path = "/i/" + tag.getAttributes().getValue("width") + 'x' + tag.getAttributes().getValue("height") + path;
            if (isCdnEnabled(tag)) {
                tag.getAttributes().setAttribute(ATTRIBUTE_NAME, cdnUrl(baseCdnUrls, path));
                tag.getAttributes().removeAttribute("cdn");
            } else {
                tag.getAttributes().setAttribute(ATTRIBUTE_NAME, baseUrl + path);
            }
        } else if (isScriptElement(tag)) {
            if (isCdnEnabled(tag)) {
                tag.getAttributes().setAttribute(ATTRIBUTE_NAME, cdnUrl(baseCdnUrls, path));
                tag.getAttributes().removeAttribute("cdn");
            } else {
                tag.getAttributes().setAttribute(ATTRIBUTE_NAME, baseUrl + path);
            }
        } else {
            throw new Error(String.format("attribute href is not allowed on element %s", tag.getElementName()));
        }
    }

    String src(ITemplateProcessingContext processingContext, IProcessableElementTag tag) {
        String src = tag.getAttributes().getValue(ATTRIBUTE_NAME);
        if (isRelativePath(src)) {
            return template(processingContext).resolve(src).toString();
        }
        return src;
    }
}
