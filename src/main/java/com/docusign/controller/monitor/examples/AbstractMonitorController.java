package com.docusign.controller.monitor.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.AuthType;
import com.docusign.core.model.Session;
import com.docusign.monitor.api.DataSetApi;
import com.docusign.monitor.client.ApiClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

/**
 * Abstract base class for all Monitor controllers.
 */
@Controller
public abstract class AbstractMonitorController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/monitor/examples/";

    public AbstractMonitorController(DSConfiguration config, String exampleName) {
        super(config, exampleName);
    }

    protected String getExamplePagesPath() {
        return AbstractMonitorController.EXAMPLE_PAGES_PATH;
    }

    protected String ensureUsageOfJWTToken(String accessToken, Session session) {
        if (session.getAuthTypeSelected() != AuthType.JWT || accessToken.isEmpty()){
            return REDIRECT_SELECT_API_PAGE;
        } else {
            return accessToken;
        }
    }

    /**
     * Creates new instance of the Monitor API client.
     * @param accessToken user's access token
     * @param session active session
     * @return an instance of the {@link com.docusign.monitor.client.ApiClient}
     */
    protected static ApiClient createApiClient(String accessToken, Session session) {

        // Step 2 start
        ApiClient apiClient = new ApiClient(ApiClient.DEMO_REST_BASEPATH);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + accessToken);
        apiClient.setBasePath(ApiClient.DEMO_REST_BASEPATH);

        // Step 2 end
        return apiClient;
    }

    /**
     * Creates a new instance of the DataSetApi. This method
     * creates an instance of the ApiClient class silently.
     * @param accessToken user's access token
     * @param session active session
     * @return an instance of the {@link DataSetApi}
     */
    protected static DataSetApi createDataSetApi(String accessToken, Session session) {
        ApiClient apiClient = createApiClient(accessToken, session);
        return new DataSetApi(apiClient);
    }
}

