package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;


/**
 * Abstract base class for all eSignature controllers.
 */
@Controller
public abstract class AbstractEsignatureController extends AbstractController {

    protected static final String MODEL_ENVELOPE_OK = "envelopeOk";
    protected static final String MODEL_TEMPLATE_OK = "templateOk";
    private static final String EXAMPLE_PAGES_PATH = "pages/esignature/examples/";

    public AbstractEsignatureController(DSConfiguration config, String exampleName) {
        super(config, exampleName);
    }

    protected String getExamplePagesPath() {
        return AbstractEsignatureController.EXAMPLE_PAGES_PATH;
    }

    /**
     * Creates new instance of the eSignature API client.
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        return apiClient;
    }

    /**
     * Creates a new instance of the eSignature EnvelopesApi. This method
     * creates an instance of the ApiClient class silently.
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link EnvelopesApi}
     */
    protected static EnvelopesApi createEnvelopesApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new EnvelopesApi(apiClient);
    }

    /**
     * Creates a new instance of the eSignature TemplatesApi. This method
     * creates an instance of the ApiClient class silently.
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link TemplatesApi}
     */
    protected static TemplatesApi createTemplatesApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new TemplatesApi(apiClient);
    }

    /**
     * Creates a new instance of the eSignature AccountsApi. This method
     * creates an instance of the ApiClient class silently.
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link AccountsApi}
     */
    protected static AccountsApi createAccountsApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new AccountsApi(apiClient);
    }

    /**
     * Get the email address of the authenticated user
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return users email address
     */
    protected static String getAuthenticatedUserEmail(String basePath, String userAccessToken) throws ApiException {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        OAuth.UserInfo userInfo = apiClient.getUserInfo(userAccessToken);

        return userInfo.getEmail();
    }

    /**
     * Get the name of the authenticated user
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return users email address
     */
    protected static String getAuthenticatedUserName(String basePath, String userAccessToken) throws ApiException {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        OAuth.UserInfo userInfo = apiClient.getUserInfo(userAccessToken);

        return userInfo.getName();
    }
}
