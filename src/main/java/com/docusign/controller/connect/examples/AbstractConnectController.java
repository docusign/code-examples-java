package com.docusign.controller.connect.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Connect controllers.
 */
@Controller
public abstract class AbstractConnectController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/connect/examples/";

    public AbstractConnectController(DSConfiguration config, String exampleName) {
        super(config, exampleName);
    }

    protected String getExamplePagesPath() {
        return AbstractConnectController.EXAMPLE_PAGES_PATH;
    }
}
