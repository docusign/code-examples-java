package com.docusign.controller.monitor.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Click controllers.
 */
@Controller
public abstract class AbstractMonitorController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/monitor/examples/";

    public AbstractMonitorController(DSConfiguration config, String exampleName, String title) {
        super(config, exampleName, title);
    }

    protected String getExamplePagesPath() {
        return AbstractMonitorController.EXAMPLE_PAGES_PATH;
    }
}

