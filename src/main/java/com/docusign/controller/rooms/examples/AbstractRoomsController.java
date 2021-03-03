package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.controller.AbstractController;
import com.docusign.rooms.api.*;
import com.docusign.rooms.client.ApiClient;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.auth.OAuth;
import com.docusign.rooms.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Abstract base class for all Rooms controllers.
 */
@Controller
public abstract class AbstractRoomsController extends AbstractController {

    private static final String EXAMPLE_PAGES_PATH = "pages/rooms/examples/";

    public AbstractRoomsController(DSConfiguration config, String exampleName, String title) {
        super(config, exampleName, title);
    }

    protected String getExamplePagesPath() {
        return AbstractRoomsController.EXAMPLE_PAGES_PATH;
    }

    /**
     * Creates new instance of the Rooms API client.
     * @param basePath URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        apiClient.addAuthorization("docusignAccessCode", new OAuth());
        return apiClient;
    }

    /**
     * Creates a new instance of the RoomsApi. This method
     * creates an instance of the ApiClient class silently.
     * @param basePath URL to Rooms REST API
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
     * @param basePath URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link RolesApi}
     */
    protected static RolesApi createRolesApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new RolesApi(apiClient);
    }

    /**
     * Creates a new instance of the rooms OfficesApi. This method
     * creates an instance of the ApiClient class silently.
     * @param basePath URL to Rooms REST API
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
     * @param basePath URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ExternalFormFillSessionsApi}
     */
    protected static ExternalFormFillSessionsApi createExternalFormFillSessionsApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new ExternalFormFillSessionsApi(apiClient);
    }

    /**
     * Creates a new instance of the rooms RoomTemplatesApi. This method
     * creates an instance of the ApiClient class silently.
     * @param basePath URL to Rooms REST API
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
     * @param basePath URL to Rooms REST API
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
     * @param basePath URL to Rooms REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link RoomsApi}
     */
    protected static FormGroupsApi createFormGroupsApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new FormGroupsApi(apiClient);
    }

    protected static RoleSummary getAdminRole(String basePath, String userAccessToken, String accountId) throws ApiException {
        RoleSummaryList roleSummaryList = createRolesApiClient(basePath, userAccessToken).getRoles(accountId);
        return roleSummaryList.getRoles()
                .stream()
                .filter(RoleSummary::isIsDefaultForAdmin)
                .findFirst()
                .get();
    }

    protected static List<FormSummary> getFormSummaryList(String basePath, String userAccessToken, String accountId) throws ApiException {
        FormLibrariesApi formLibrariesApi = createFormLibrariesApi(basePath, userAccessToken);
        FormLibrarySummaryList formLibrarySummaryList = formLibrariesApi.getFormLibraries(accountId);

        List<FormSummary> forms = new ArrayList<>();

        for (FormLibrarySummary formLibrarySummary : formLibrarySummaryList.getFormsLibrarySummaries()) {
            FormSummaryList formSummaryList = formLibrariesApi.getFormLibraryForms(accountId, formLibrarySummary.getFormsLibraryId());
            forms.addAll(formSummaryList.getForms());
        }
        return forms;
    }

    protected List<RoomDocument> getDocumentsByRoomsId(String basePath, String accessToken, String accountId, WorkArguments roomId) throws ApiException {
        RoomsApi roomsApiClient = createRoomsApiClient(basePath, accessToken);

        return roomsApiClient.getDocuments(accountId, roomId.getRoomId()).getDocuments();
    }

    protected RoomSummaryList getRoomSummaryList(String basePath, String accessToken, String accountId) throws ApiException {
        RoomsApi roomsApi = createRoomsApiClient(basePath, accessToken);

        return roomsApi.getRooms(accountId);
    }

    protected List<RoomSummary> getRoomSummariesByRoomId(String basePath, String accessToken, String accountId, WorkArguments roomId) throws ApiException {
        RoomSummaryList summaryList = getRoomSummaryList(basePath, accessToken, accountId);
        return summaryList.getRooms().stream()
                .filter(var -> var.getRoomId().equals(roomId.getRoomId()))
                .collect(Collectors.toList());
    }

    protected List<FormSummary> getAvailableFormSummariesByRoomId(String basePath, String accessToken, String accountId, WorkArguments roomId) throws ApiException {
        List<RoomDocument> formsByRoomsId = getDocumentsByRoomsId(basePath, accessToken, accountId, roomId);
        List<FormSummary> formSummaries = getFormSummaryList(basePath, accessToken, accountId);

        List<String> formListByRoomId = formsByRoomsId.stream()
                .map(var -> var.getName().replaceAll("\\.pdf", ""))
                .collect(Collectors.toList());

        return formSummaries.stream()
                .filter(form -> !formListByRoomId.contains(form.getName()))
                .collect(Collectors.toList());
    }
}
