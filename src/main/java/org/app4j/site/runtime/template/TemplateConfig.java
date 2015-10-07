package org.app4j.site.runtime.template;

import org.app4j.site.runtime.template.service.Template;
import org.app4j.site.runtime.template.service.TemplateDialect;
import org.app4j.site.runtime.template.service.TemplateRepository;
import org.thymeleaf.TemplateEngine;

import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public interface TemplateConfig {
    TemplateDialect dialect();

    TemplateEngine engine();

    AssetsConfig assets();

    TemplateConfig add(TemplateRepository resourceRepository);

    Optional<Template> get(String templatePath);

    List<Template> all();
}
