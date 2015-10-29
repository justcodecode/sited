package org.app4j.site.internal.template;

import org.app4j.site.internal.template.service.TemplateDialect;
import org.thymeleaf.TemplateEngine;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public interface TemplateConfig {
    TemplateDialect dialect();

    TemplateEngine engine();

    AssetsConfig assets();

    Optional<Template> get(String templatePath);

    File dir();

    List<Template> all();
}
