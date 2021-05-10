package com.docusign.controller.monitor.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.AuthType;
import com.docusign.core.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Monitor controllers.
 */
@Controller
public abstract class AbstractMonitorController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/monitor/examples/";

    protected static String apiUrl = "/v2.0/datasets/monitor";

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    public AbstractMonitorController(DSConfiguration config, String exampleName, String title) {
        super(config, exampleName, title);
    }

    protected String getExamplePagesPath() {
        return AbstractMonitorController.EXAMPLE_PAGES_PATH;
    }

    protected String ensureUsageOfJWTToken(String accessToken, Session session) {
        if (session.getAuthTypeSelected() != AuthType.JWT || accessToken.isEmpty()){
            return REDIRECT_AUTHENTICATION_PAGE;
        } else {
            return accessToken;
        }
    }
}

