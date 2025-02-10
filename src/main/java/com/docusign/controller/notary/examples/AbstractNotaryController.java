package com.docusign.controller.notary.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all controllers.
 */
@Controller
public abstract class AbstractNotaryController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/notary/examples/";

    protected Session session;

    protected User user;

    public AbstractNotaryController(DSConfiguration config, String exampleName, Session session, User user) {
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

    //ds-snippet-start:Notary4Step2
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        apiClient.addAuthorization("docusignAccessCode", new OAuth());
        return apiClient;
    }
    //ds-snippet-end:Notary4Step2

    /**
     * Creates a new instance of the eSignature EnvelopesApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link EnvelopesApi}
     */
    protected EnvelopesApi createEnvelopesApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new EnvelopesApi(apiClient);
    }

    protected String getExamplePagesPath() {
        return AbstractNotaryController.EXAMPLE_PAGES_PATH;
    }
}
