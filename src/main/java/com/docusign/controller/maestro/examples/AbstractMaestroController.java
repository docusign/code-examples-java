package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Maestro fields controllers.
 */
@Controller
public abstract class AbstractMaestroController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/maestro/examples/";

    protected final User user;

    protected final Session session;

    public AbstractMaestroController(DSConfiguration config, String exampleName, User user, Session session) {
        super(config, exampleName);
        this.user = user;
        this.session = session;
    }

    protected String getExamplePagesPath() {
        return AbstractMaestroController.EXAMPLE_PAGES_PATH;
    }
}
