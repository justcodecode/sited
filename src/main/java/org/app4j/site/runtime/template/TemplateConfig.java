package org.app4j.site.runtime.template;

import org.thymeleaf.TemplateEngine;

/**
 * @author chi
 */
public interface TemplateConfig {
    TemplateDialect dialect();

    TemplateEngine engine();

    AssetsConfig assets();

    TemplateConfig add(ResourceRepository resourceRepository);
}
