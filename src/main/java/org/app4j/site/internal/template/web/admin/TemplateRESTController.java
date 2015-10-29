package org.app4j.site.internal.template.web.admin;

import org.app4j.site.internal.template.TemplateConfig;
import org.app4j.site.internal.template.util.Git;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;

/**
 * @author chi
 */
public class TemplateRESTController {
    private final TemplateConfig templateConfig;

    public TemplateRESTController(TemplateConfig templateConfig) {
        this.templateConfig = templateConfig;
    }

    public Response pull(Request request) throws Exception {
        Git git = new Git(templateConfig.dir());
        GitPullResponse response = new GitPullResponse();
        response.success = git.pull();
        response.errors = git.errors();
        response.updates = git.updates();
        return Response.bean(response);
    }
}
