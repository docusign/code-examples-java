package com.docusign.controller.navigator.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Navigator fields controllers.
 */
@Controller
public abstract class AbstractNavigatorController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/navigator/examples/";

    protected final User user;

    protected final Session session;

    public AbstractNavigatorController(DSConfiguration config, String exampleName, User user, Session session) {
        super(config, exampleName);
        this.user = user;
        this.session = session;
    }

    protected String getExamplePagesPath() {
        return AbstractNavigatorController.EXAMPLE_PAGES_PATH;
    }
}
