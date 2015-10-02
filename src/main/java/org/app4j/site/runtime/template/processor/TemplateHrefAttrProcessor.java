package org.app4j.site.runtime.template.processor;

import com.google.common.base.Strings;
import org.app4j.site.runtime.template.TemplateDialect;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

public class TemplateHrefAttrProcessor extends AbstractAttributeTagProcessor implements TemplateProcessorSupport {
    public static final int PRECEDENCE = 1000;
    public static final String HREF = "href";

    private final String baseUrl;
    private final List<String> baseCdnUrls;

    public TemplateHrefAttrProcessor(TemplateDialect dialect, String baseUrl, List<String> baseCdnUrls) {
        super(dialect, TemplateMode.HTML, dialect.getPrefix(), null, false, HREF, true, PRECEDENCE, true);
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.baseCdnUrls = baseCdnUrls;
    }

    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        String path = attributeValue == null ? tag.getAttributes().getValue(HREF) : evalAsString(attributeValue, processingContext);

        if (Strings.isNullOrEmpty(path)) {
            return;
        }

        path = path.startsWith("/") ? path : "/" + path;

        if (isLinkElement(tag)) {
            tag.getAttributes().setAttribute(HREF, baseUrl + path);
        } else if (isCssElement(tag)) {
            if (isCdnEnabled(tag)) {
                tag.getAttributes().setAttribute(HREF, cdnUrl(baseCdnUrls, path));
                tag.getAttributes().removeAttribute("cdn");
            } else {
                tag.getAttributes().setAttribute(HREF, baseUrl + path);
            }
        } else {
            throw new Error(String.format("attribute href is not allowed on element %s", tag.getElementName()));
        }
    }
}
