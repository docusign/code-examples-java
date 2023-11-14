package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiClient;
import com.docusign.click.client.auth.OAuth;
import com.docusign.core.controller.AbstractController;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Click controllers.
 */
@Controller
public abstract class AbstractClickController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/click/examples/";

    public AbstractClickController(DSConfiguration config, String exampleName) {
        super(config, exampleName);
    }

    /**
     * Creates new instance of the Click API client.
     *
     * @param basePath        URL to Click REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        //ds-snippet-start:ClickJavaStep2
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        apiClient.addAuthorization("docusignAccessCode", new OAuth());
        return apiClient;
        //ds-snippet-end:ClickJavaStep2
    }

    /**
     * Creates a new instance of the ClickApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Click REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link AccountsApi}
     */
    protected static AccountsApi createAccountsApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new AccountsApi(apiClient);
    }

    protected String getExamplePagesPath() {
        return AbstractClickController.EXAMPLE_PAGES_PATH;
    }
}

