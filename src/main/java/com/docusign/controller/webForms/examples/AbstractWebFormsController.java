package com.docusign.controller.webForms.examples;

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
public abstract class AbstractWebFormsController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/webforms/examples/";

    protected Session session;

    protected User user;

    public AbstractWebFormsController(DSConfiguration config, String exampleName, Session session, User user) {
        super(config, exampleName);
        this.session = session;
        this.user = user;
    }

    /**
     * Creates new instance of the Rooms API client.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */

    protected static ApiClient createESignApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        apiClient.addAuthorization("docusignAccessCode", new OAuth());
        return apiClient;
    }

    protected static com.docusign.webforms.client.ApiClient createWebFormsApiClient(
            String basePath,
            String userAccessToken
    ) {
       //ds-snippet-start:WebForms1Step2
        com.docusign.webforms.client.ApiClient apiClient = new com.docusign.webforms.client.ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        apiClient.addAuthorization("docusignAccessCode", new com.docusign.webforms.client.auth.OAuth());
       //ds-snippet-end:WebForms1Step2
       return apiClient;
    }
    
    protected String getExamplePagesPath() {
        return AbstractWebFormsController.EXAMPLE_PAGES_PATH;
    }
}
