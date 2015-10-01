package org.app4j.site.runtime.template.processor;

import com.google.common.base.Strings;
import org.app4j.site.Site;
import org.app4j.site.util.JSON;
import org.app4j.site.util.Value;
import org.app4j.site.web.Request;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.List;

/**
 * @author chi
 */
public interface TemplateProcessorSupport {
    default Object eval(String content, ITemplateProcessingContext processingContext) {
        if (Strings.isNullOrEmpty(content)) {
            return null;
        }
        final IEngineConfiguration configuration = processingContext.getConfiguration();
        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression = expressionParser.parseExpression(processingContext, "|" + content + "|");
        return expression.execute(processingContext);
    }

    default String evalAsString(String content, ITemplateProcessingContext processingContext) {
        Object value = eval(content, processingContext);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    default Integer evalAsInteger(String content, ITemplateProcessingContext processingContext, Integer defaultValue) {
        String value = evalAsString(content, processingContext);
        if (!Strings.isNullOrEmpty(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    default boolean isLinkElement(IProcessableElementTag tag) {
        return "a".equals(tag.getElementName());
    }

    default boolean isCssElement(IProcessableElementTag tag) {
        return "link".equals(tag.getElementName());
    }

    default boolean isScriptElement(IProcessableElementTag tag) {
        return "script".equals(tag.getElementName());
    }

    default boolean isImageElement(IProcessableElementTag tag) {
        return "img".equals(tag.getElementName());
    }

    default boolean isCdnEnabled(IProcessableElementTag tag) {
        return "true".equals(tag.getAttributes().getValue("cdn"));
    }

    default String cdnUrl(List<String> baseCdnUrls, String path) {
        return baseCdnUrls.get(Math.abs(path.hashCode()) % baseCdnUrls.size()) + path;
    }

    default Site site(ITemplateProcessingContext iTemplateProcessingContext) {
        return (Site) iTemplateProcessingContext.getVariables().getVariable("__site__");
    }

    default Request request(ITemplateProcessingContext iTemplateProcessingContext) {
        return (Request) iTemplateProcessingContext.getVariables().getVariable("__request__");
    }

    default <T> Value<T> attribute(String attributeName, IProcessableElementTag tag, Class<T> type) {
        String attributeValue = tag.getAttributes().getValue(attributeName);
        if (attributeValue != null) {
            return new Value<>(attributeName, JSON.mapper().convertValue(attributeValue, type));
        }
        return new Value<>(attributeName, null);
    }

    default Value<String> attribute(String attributeName, IProcessableElementTag tag) {
        return this.attribute(attributeName, tag, String.class);
    }
}
