package org.app4j.site.module.page.processor;

import org.app4j.site.module.page.web.PageContext;
import org.app4j.site.runtime.template.processor.TemplateProcessorSupport;
import org.thymeleaf.context.ITemplateProcessingContext;

/**
 * @author chi
 */
public interface PageProcessorSupport extends TemplateProcessorSupport {
    default PageContext pageContext(ITemplateProcessingContext templateContext) {
        return (PageContext) templateContext.getVariables().getVariable("__page__");
    }
}
