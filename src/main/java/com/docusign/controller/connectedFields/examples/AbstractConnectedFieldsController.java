package com.docusign.controller.connectedFields.examples;

import com.docusign.DSConfiguration;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Connected fields controllers.
 */
@Controller
public abstract class AbstractConnectedFieldsController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/connectedfields/examples/";

    protected final User user;

    protected final Session session;

    public AbstractConnectedFieldsController(DSConfiguration config, String exampleName, User user, Session session) {
        super(config, exampleName);
        this.user = user;
        this.session = session;
    }

    /**
     * Creates new instance of the eSignature API client.
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(
            String accessToken,
            String basePath) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(
                HttpHeaders.AUTHORIZATION,
                BEARER_AUTHENTICATION + accessToken);

        return apiClient;
    }

    /**
     * Creates a new instance of the EnvelopesApi. This method
     * creates an instance of the EnvelopesApi class silently.
     *
     * @param accessToken user's access token
     * @param basePath    basePath to the server
     * @return an instance of the {@link EnvelopesApi}
     */
    protected EnvelopesApi createEnvelopesApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(userAccessToken, basePath);
        return new EnvelopesApi(apiClient);
    }

    protected String getExamplePagesPath() {
        return AbstractConnectedFieldsController.EXAMPLE_PAGES_PATH;
    }
}
