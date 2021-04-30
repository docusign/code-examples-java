package com.docusign.controller.monitor.examples;

import com.docusign.DSConfiguration;
import com.docusign.WebSecurityConfig;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.AuthType;
import com.docusign.core.model.Session;
import com.docusign.core.security.jwt.JWTAuthorizationCodeResourceDetails;
import com.docusign.core.security.jwt.JWTOAuth2RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Monitor controllers.
 */
@Controller
public abstract class AbstractMonitorController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/monitor/examples/";

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    public AbstractMonitorController(DSConfiguration config, String exampleName, String title) {
        super(config, exampleName, title);
    }

    protected String getExamplePagesPath() {
        return AbstractMonitorController.EXAMPLE_PAGES_PATH;
    }

    protected void ensureUsageOfJWTToken(String accessToken, Session session) {
        if (session.getAuthTypeSelected() != AuthType.JWT || accessToken.isEmpty()){
            WebSecurityConfig securityConfig = new WebSecurityConfig();
            JWTAuthorizationCodeResourceDetails jwtCodeGrantClient = securityConfig.jwtCodeGrantClient();

            OAuth2RestTemplate restTemplate = new JWTOAuth2RestTemplate(jwtCodeGrantClient, oAuth2ClientContext);
            accessToken = restTemplate.getAccessToken().toString();
        }
    }
}

