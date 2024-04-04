package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.maestro.client.ApiClient;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Maestro controllers.
 */
@Controller
public abstract class AbstractMaestroController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/maestro/examples/";

    protected final Session session;

    protected final User user;

    public AbstractMaestroController(DSConfiguration config, String exampleName, Session session, User user) {
        super(config, exampleName);
        this.session = session;
        this.user = user;
    }

    /**
     * Creates new instance of the Maestro API client.
     *
     * @param basePath        URL to Maestro REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        return apiClient;
    }

    protected String getExamplePagesPath() {
        return AbstractMaestroController.EXAMPLE_PAGES_PATH;
    }
}
