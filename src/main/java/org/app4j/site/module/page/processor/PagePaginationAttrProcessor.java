package org.app4j.site.module.page.processor;

import org.app4j.site.runtime.database.Pageable;
import org.app4j.site.runtime.template.TemplateDialect;
import org.app4j.site.runtime.template.processor.TemplateProcessorSupport;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class PagePaginationAttrProcessor extends AbstractElementTagProcessor implements TemplateProcessorSupport {
    public static final int PRECEDENCE = 1000;
    public static final String ELEMENT_NAME = "pagination";
    private final String baseUrl;

    public PagePaginationAttrProcessor(IProcessorDialect dialect, String baseUrl) {
        super(dialect, TemplateMode.HTML, TemplateDialect.PREFIX, ELEMENT_NAME, true, null, false, PRECEDENCE);
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, String tagTemplateName, int tagLine, int tagCol, IElementTagStructureHandler structureHandler) {
        Pageable<?> findView = (Pageable<?>) eval(tag.getAttributes().getValue("pageable"), processingContext);

        int pageSize = evalAsInteger(tag.getAttributes().getValue("size"), processingContext, 10);
        int display = evalAsInteger(tag.getAttributes().getValue("display"), processingContext, 20);
        String prefix = evalAsString(tag.getAttributes().getValue("prefix"), processingContext);

        int current = (int) findView.offset() / pageSize + 1;
        int total = (int) (findView.total() % pageSize == 0 ? findView.total() / pageSize : findView.total() / pageSize + 1);

        int start = current - display / 2 > 0 ? current - display / 2 : 1;
        int end = current + display / 2 < total ? current + display / 2 : total;

        StringBuilder b = new StringBuilder(32);
        b.append("<ul class=\"pagination\">");
        for (int i = start; i <= end; i++) {
            if (i == current) {
                b.append("<li class=\"active\">");
            } else {
                b.append("<li>");
            }
            b.append("<a href=\"").append(baseUrl).append(prefix).append(i)
                    .append("/\">")
                    .append(i)
                    .append("</a></li>");

        }
        b.append("</ul>");

        structureHandler.removeBody();
        tag.getAttributes().removeAttribute("prefix");
        structureHandler.replaceWith(b.toString(), false);
    }
}