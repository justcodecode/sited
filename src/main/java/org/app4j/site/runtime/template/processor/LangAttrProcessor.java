package org.app4j.site.runtime.template.processor;

import org.app4j.site.runtime.template.TemplateDialect;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author chi
 */
public class LangAttrProcessor extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "lang";

    public LangAttrProcessor(IProcessorDialect dialect) {
        super(dialect, TemplateMode.HTML, TemplateDialect.PREFIX, ATTR_NAME, PRECEDENCE, true);
    }


    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, Object expressionResult, IElementTagStructureHandler structureHandler) {
        tag.getAttributes().setAttribute("lang", expressionResult.toString());
    }
}
