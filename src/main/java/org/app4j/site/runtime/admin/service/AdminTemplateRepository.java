package org.app4j.site.runtime.admin.service;

import com.google.common.collect.Lists;
import org.app4j.site.runtime.template.service.Template;
import org.app4j.site.runtime.template.service.TemplateRepository;
import org.app4j.site.util.ResourceRepository;

import java.util.Iterator;

/**
 * @author chi
 */
public class AdminTemplateRepository extends TemplateRepository {
    public AdminTemplateRepository(ResourceRepository resourceRepository) {
        super(resourceRepository);
    }

    @Override
    public Iterator<Template> iterator() {
        return Lists.<Template>newArrayList().iterator();
    }
}
