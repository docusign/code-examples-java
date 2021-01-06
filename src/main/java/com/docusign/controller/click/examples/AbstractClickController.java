package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiClient;
import com.docusign.click.client.ApiException;
import com.docusign.click.client.auth.OAuth;
import com.docusign.click.model.ClickwrapResponse;
import com.docusign.core.controller.AbstractController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Click controllers.
 */
@Controller
public abstract class AbstractClickController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/click/examples/";

    public AbstractClickController(DSConfiguration config, String exampleName, String title) {
        super(config, exampleName, title);
    }

    protected String getExamplePagesPath() {
        return AbstractClickController.EXAMPLE_PAGES_PATH;
    }

    /**
     * Creates new instance of the Click API client.
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        // it is a workaround to NPE, see DSPW-61
        // apiClient.addAuthorization("docusignAccessCode", null);
        apiClient.addAuthorization("docusignAccessCode", new OAuth());
        return apiClient;
    }

    /**
     * Creates a new instance of the ClickApi. This method
     * creates an instance of the ApiClient class silently.
     * @param basePath URL to Click REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link AccountsApi}
     */
    protected static AccountsApi createAccountsApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new AccountsApi(apiClient);
    }

    /**
     * Determine if clickwrap is exists and is active.
     * @param basePath URL to Click API
     * @param userAccessToken user's access token
     * @param accountId user's account id
     * @param clickwrapId clickwrap's id
     * @return an boolean value
     */
    protected boolean isClickwrapExistsAndActive(String basePath, String userAccessToken,
                                                 String accountId, String clickwrapId) throws ApiException {
        boolean isClickwrapOk = false;

        if (StringUtils.isNotBlank(clickwrapId)) {

            AccountsApi accountsApi = this.createAccountsApiClient(basePath, userAccessToken);
            ClickwrapResponse clickwrapResponse = accountsApi.getClickwrap(accountId, clickwrapId);

            isClickwrapOk = clickwrapResponse.getClickwrapId().equals(clickwrapId) &&
                    clickwrapResponse.getStatus().equals(ClickwrapHelper.STATUS_ACTIVE);
        }

        return isClickwrapOk;
    }
}

