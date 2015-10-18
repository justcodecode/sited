package org.app4j.site.module.page.processor;

import com.google.common.collect.Maps;
import org.app4j.site.DefaultScope;
import org.app4j.site.module.page.variable.VariableConfig;
import org.app4j.site.module.page.variable.VariableRef;
import org.app4j.site.runtime.template.processor.TemplateProcessorSupport;
import org.thymeleaf.context.ITemplateProcessingContext;
import org.thymeleaf.context.TemplateProcessingContext;
import org.thymeleaf.context.VariablesMap;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

/**
 * @author chi
 */
public class RequireElementTagProcessor extends AbstractElementTagProcessor implements TemplateProcessorSupport {
    private final VariableConfig variableConfig;

    public RequireElementTagProcessor(IProcessorDialect dialect, VariableConfig variableConfig) {
        super(dialect, TemplateMode.HTML, "j", "require", true, null, false, 1000);
        this.variableConfig = variableConfig;
    }

    @Override
    protected void doProcess(ITemplateProcessingContext processingContext, IProcessableElementTag tag, String tagTemplateName, int tagLine, int tagCol, IElementTagStructureHandler structureHandler) {
        TemplateProcessingContext templateProcessingContext = (TemplateProcessingContext) processingContext;
        VariablesMap variables = (VariablesMap) templateProcessingContext.getVariables();

        Map<String, Object> parameters = Maps.newHashMap();
        for (AttributeName attributeName : tag.getAttributes().getAllAttributeNames()) {
            parameters.put(attributeName.getAttributeName(), eval(tag.getAttributes().getValue(attributeName), processingContext));
        }

        String name = tag.getAttributes().getValue("name");
        String as = tag.getAttributes().getValue("as");
        VariableRef variableRef = new VariableRef(name, as, parameters);
        TemplateScope templateScope = new TemplateScope((DefaultScope) request(processingContext));

        for (String variableName : processingContext.getVariables().getVariableNames()) {
            Object value = processingContext.getVariables().getVariable(variableName);
            bind(templateScope, variableName, value);
        }

        Object value = variableConfig.eval(variableRef, templateScope);

        variables.decreaseLevel();
        variables.put(variableRef.param("as").orElse(name).get(), value);
        variables.increaseLevel();

        structureHandler.removeElement();
    }

    @SuppressWarnings("unchecked")
    <T> void bind(TemplateScope templateScope, String name, T value) {
        templateScope.bind((Class<T>) value.getClass()).named(name).to(value);
    }
}
