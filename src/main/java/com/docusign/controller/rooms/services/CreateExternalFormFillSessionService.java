package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.ExternalFormFillSessionsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.ExternalFormFillSession;
import com.docusign.rooms.model.ExternalFormFillSessionForCreate;

public final class CreateExternalFormFillSessionService {
    public static ExternalFormFillSession createExternalFormFillSession(
            ExternalFormFillSessionsApi externalFormFillSessionsApi,
            String accountId,
            String formId,
            Integer roomId,
            String xframeURL
    ) throws ApiException {
        // Construct your request body
        ExternalFormFillSessionForCreate externalFormFillSessionForCreate = new ExternalFormFillSessionForCreate()
                .formId(formId)
                .roomId(roomId)
                .xFrameAllowedUrl(xframeURL);

        // Step 4. Call the v2 Rooms API
        //ds-snippet-start:Rooms6Step4
        return externalFormFillSessionsApi.createExternalFormFillSession(
                accountId, externalFormFillSessionForCreate
        );
        //ds-snippet-end:Rooms6Step4
    }
}
