package org.app4j.site.module.page.processor;

import org.app4j.site.internal.template.processor.TemplateProcessorSupport;
import org.app4j.site.module.page.web.PageContext;
import org.thymeleaf.context.ITemplateProcessingContext;

/**
 * @author chi
 */
public interface PageProcessorSupport extends TemplateProcessorSupport {
    default PageContext pageContext(ITemplateProcessingContext templateContext) {
        return (PageContext) templateContext.getVariables().getVariable("__page__");
    }
}
