package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.core.controller.AbstractController;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.*;
import com.docusign.rooms.client.ApiClient;
import com.docusign.rooms.client.auth.OAuth;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;


/**
 * Abstract base class for all Rooms controllers.
 */
@Controller
public abstract class AbstractRoomsController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/rooms/examples/";

    protected Session session;

    protected User user;

    public AbstractRoomsController(DSConfiguration config, String exampleName) {
        super(config, exampleName);
    }

    /**
     * Creates new instance of the Rooms API client.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */

    //ds-snippet-start:RoomsJavaStep2
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        apiClient.addAuthorization("docusignAccessCode", new OAuth());
        return apiClient;
    }

    /**
     * Creates a new instance of the RoomsApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link RoomsApi}
     */
    protected static RoomsApi createRoomsApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new RoomsApi(apiClient);
    }

    /**
     * Creates a new instance of the rooms RolesApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link RolesApi}
     */
    protected static RolesApi createRolesApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new RolesApi(apiClient);
    }
    //ds-snippet-end:RoomsJavaStep2

    /**
     * Creates a new instance of the rooms OfficesApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link OfficesApi}
     */
    protected static OfficesApi createOfficesApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new OfficesApi(apiClient);
    }

    /**
     * Creates a new instance of the rooms ExternalFormFillSessionsApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ExternalFormFillSessionsApi}
     */
    protected static ExternalFormFillSessionsApi createExternalFormFillSessionsApiClient(
            String basePath,
            String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new ExternalFormFillSessionsApi(apiClient);
    }

    /**
     * Creates a new instance of the rooms RoomTemplatesApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link RoomTemplatesApi}
     */
    protected static RoomTemplatesApi createRoomTemplatesApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new RoomTemplatesApi(apiClient);
    }

    /**
     * Creates a new instance of the FormLibrariesApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link RoomsApi}
     */
    protected static FormLibrariesApi createFormLibrariesApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new FormLibrariesApi(apiClient);
    }

    /**
     * Creates a new instance of the FormGroupsApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link RoomsApi}
     */
    protected static FormGroupsApi createFormGroupsApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new FormGroupsApi(apiClient);
    }

    protected String getExamplePagesPath() {
        return AbstractRoomsController.EXAMPLE_PAGES_PATH;
    }
}
