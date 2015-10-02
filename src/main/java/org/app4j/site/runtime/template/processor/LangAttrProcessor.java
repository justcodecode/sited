package org.app4j.site.runtime.template.processor;

import org.app4j.site.runtime.template.TemplateDialect;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author chi
 */
public class LangAttrProcessor extends AbstractAttributeTagProcessor implements TemplateProcessorSupport {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "lang";

    public LangAttrProcessor(TemplateDialect dialect) {
        super(dialect, TemplateMode.HTML, dialect.getPrefix(), null, false, ATTR_NAME, true, PRECEDENCE, true);
    }

    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler) {
        tag.getAttributes().setAttribute("lang", evalAsString(attributeValue, processingContext));
    }
}
