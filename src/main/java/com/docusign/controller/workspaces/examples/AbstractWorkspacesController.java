package com.docusign.controller.workspaces.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all controllers.
 */
@Controller
public abstract class AbstractWorkspacesController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/workspaces/examples/";

    protected Session session;

    protected User user;

    public AbstractWorkspacesController(DSConfiguration config, String exampleName, Session session, User user) {
        super(config, exampleName);
        this.session = session;
        this.user = user;
    }
    
    protected String getExamplePagesPath() {
        return AbstractWorkspacesController.EXAMPLE_PAGES_PATH;
    }
}
