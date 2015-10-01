package org.app4j.site.runtime.template;

import com.google.common.collect.Sets;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardActionTagProcessor;
import org.thymeleaf.standard.processor.StandardAltTitleTagProcessor;
import org.thymeleaf.standard.processor.StandardAssertTagProcessor;
import org.thymeleaf.standard.processor.StandardAttrTagProcessor;
import org.thymeleaf.standard.processor.StandardAttrappendTagProcessor;
import org.thymeleaf.standard.processor.StandardAttrprependTagProcessor;
import org.thymeleaf.standard.processor.StandardBlockTagProcessor;
import org.thymeleaf.standard.processor.StandardCaseTagProcessor;
import org.thymeleaf.standard.processor.StandardClassappendTagProcessor;
import org.thymeleaf.standard.processor.StandardConditionalCommentProcessor;
import org.thymeleaf.standard.processor.StandardConditionalFixedValueTagProcessor;
import org.thymeleaf.standard.processor.StandardDOMEventAttributeTagProcessor;
import org.thymeleaf.standard.processor.StandardEachTagProcessor;
import org.thymeleaf.standard.processor.StandardFragmentTagProcessor;
import org.thymeleaf.standard.processor.StandardIfTagProcessor;
import org.thymeleaf.standard.processor.StandardIncludeTagProcessor;
import org.thymeleaf.standard.processor.StandardInlineHTMLTagProcessor;
import org.thymeleaf.standard.processor.StandardInliningTextProcessor;
import org.thymeleaf.standard.processor.StandardInsertTagProcessor;
import org.thymeleaf.standard.processor.StandardLangXmlLangTagProcessor;
import org.thymeleaf.standard.processor.StandardMethodTagProcessor;
import org.thymeleaf.standard.processor.StandardNonRemovableAttributeTagProcessor;
import org.thymeleaf.standard.processor.StandardObjectTagProcessor;
import org.thymeleaf.standard.processor.StandardRemovableAttributeTagProcessor;
import org.thymeleaf.standard.processor.StandardRemoveTagProcessor;
import org.thymeleaf.standard.processor.StandardReplaceTagProcessor;
import org.thymeleaf.standard.processor.StandardStyleappendTagProcessor;
import org.thymeleaf.standard.processor.StandardSubstituteByTagProcessor;
import org.thymeleaf.standard.processor.StandardSwitchTagProcessor;
import org.thymeleaf.standard.processor.StandardTextTagProcessor;
import org.thymeleaf.standard.processor.StandardTranslationDocTypeProcessor;
import org.thymeleaf.standard.processor.StandardUnlessTagProcessor;
import org.thymeleaf.standard.processor.StandardUtextTagProcessor;
import org.thymeleaf.standard.processor.StandardValueTagProcessor;
import org.thymeleaf.standard.processor.StandardWithTagProcessor;
import org.thymeleaf.standard.processor.StandardXmlBaseTagProcessor;
import org.thymeleaf.standard.processor.StandardXmlLangTagProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.standard.processor.StandardXmlSpaceTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Set;

/**
 * @author chi
 */
public class TemplateDialect extends StandardDialect {
    public static final String PREFIX = "j";
    private final Set<IProcessor> processors = Sets.newLinkedHashSet();

    public TemplateDialect() {
        super("template", PREFIX, 1000);
        processors.add(new StandardTextTagProcessor(this, TemplateMode.HTML, PREFIX));
        /*
         * HTML: ATTRIBUTE TAG PROCESSORS
         */
        processors.add(new StandardActionTagProcessor(this, PREFIX));
        processors.add(new StandardAltTitleTagProcessor(this, PREFIX));
        processors.add(new StandardAssertTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardAttrTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardAttrappendTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardAttrprependTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardCaseTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardClassappendTagProcessor(this, PREFIX));
        for (final String attrName : StandardConditionalFixedValueTagProcessor.ATTR_NAMES) {
            processors.add(new StandardConditionalFixedValueTagProcessor(this, PREFIX, attrName));
        }
        for (final String attrName : StandardDOMEventAttributeTagProcessor.ATTR_NAMES) {
            processors.add(new StandardRemovableAttributeTagProcessor(this, PREFIX, attrName));
        }
        processors.add(new StandardEachTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardFragmentTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardIfTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardIncludeTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardInlineHTMLTagProcessor(this, PREFIX));
        processors.add(new StandardInsertTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardLangXmlLangTagProcessor(this, PREFIX));
        processors.add(new StandardMethodTagProcessor(this, PREFIX));
        for (final String attrName : StandardNonRemovableAttributeTagProcessor.ATTR_NAMES) {
            processors.add(new StandardNonRemovableAttributeTagProcessor(this, PREFIX, attrName));
        }
        processors.add(new StandardObjectTagProcessor(this, TemplateMode.HTML, PREFIX));
        for (final String attrName : StandardRemovableAttributeTagProcessor.ATTR_NAMES) {
            processors.add(new StandardRemovableAttributeTagProcessor(this, PREFIX, attrName));
        }
        processors.add(new StandardRemoveTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardReplaceTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardStyleappendTagProcessor(this, PREFIX));
        processors.add(new StandardSubstituteByTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardSwitchTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardTextTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardUnlessTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardUtextTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardValueTagProcessor(this, PREFIX));
        processors.add(new StandardWithTagProcessor(this, TemplateMode.HTML, PREFIX));
        processors.add(new StandardXmlBaseTagProcessor(this, PREFIX));
        processors.add(new StandardXmlLangTagProcessor(this, PREFIX));
        processors.add(new StandardXmlSpaceTagProcessor(this, PREFIX));
        processors.add(new StandardXmlNsTagProcessor(this, TemplateMode.HTML, PREFIX));
        // TODO: Implement default attribute processor
//        processors.add(new StandardDefaultAttributesTagProcessor());

        /*
         * HTML: ELEMENT TAG PROCESSORS
         */
        processors.add(new StandardBlockTagProcessor(this, TemplateMode.HTML, PREFIX, StandardBlockTagProcessor.ELEMENT_NAME));

        /*
         * HTML: TEXT PROCESSORS
         */
        processors.add(new StandardInliningTextProcessor(this, TemplateMode.HTML));

        /*
         * HTML: DOCTYPE PROCESSORS
         */
        processors.add(new StandardTranslationDocTypeProcessor(this));

        /*
         * HTML: COMMENT PROCESSORS
         */
        processors.add(new StandardConditionalCommentProcessor(this));

    }

    public TemplateDialect add(IProcessor iProcessor) {
        processors.add(iProcessor);
        return this;
    }

    @Override
    public Set<IProcessor> getProcessors(String prefix) {
        return processors;
    }
}
