package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.api.UsersApi;
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

    protected final Session session;

    protected final User user;

    public AbstractEsignatureController(DSConfiguration config, String exampleName, Session session, User user) {
        super(config, exampleName);
        this.session = session;
        this.user = user;
    }

    protected String getExamplePagesPath() {
        return AbstractEsignatureController.EXAMPLE_PAGES_PATH;
    }

    /**
     * Creates new instance of the eSignature API client.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected ApiClient createApiClient(String basePath, String userAccessToken) {
        //ds-snippet-start:eSignJavaStep2
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        //ds-snippet-end:eSignJavaStep2
        return apiClient;
    }

    /**
     * Gets the information about the currently logged user.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link OAuth.UserInfo}
     */
    protected OAuth.UserInfo getCurrentUserInfo(String basePath, String userAccessToken) throws ApiException {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return apiClient.getUserInfo(userAccessToken);
    }

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

    /**
     * Creates a new instance of the eSignature UsersApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link UsersApi}
     */
    protected UsersApi createUsersApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new UsersApi(apiClient);
    }

    /**
     * Creates a new instance of the eSignature TemplatesApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link TemplatesApi}
     */
    protected TemplatesApi createTemplatesApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new TemplatesApi(apiClient);
    }

    /**
     * Creates a new instance of the eSignature AccountsApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link AccountsApi}
     */
    protected AccountsApi createAccountsApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new AccountsApi(apiClient);
    }

    /**
     * Get the email address of the authenticated user
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return users email address
     */
    protected String getAuthenticatedUserEmail(String basePath, String userAccessToken) throws ApiException {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        OAuth.UserInfo userInfo = apiClient.getUserInfo(userAccessToken);

        return userInfo.getEmail();
    }

    /**
     * Get the name of the authenticated user
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return users email address
     */
    protected String getAuthenticatedUserName(String basePath, String userAccessToken) throws ApiException {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        OAuth.UserInfo userInfo = apiClient.getUserInfo(userAccessToken);

        return userInfo.getName();
    }
}
