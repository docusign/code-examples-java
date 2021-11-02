package com.services.rooms;

import com.docusign.rooms.api.ExternalFormFillSessionsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.ExternalFormFillSession;
import com.docusign.rooms.model.ExternalFormFillSessionForCreate;

public final class CreateExternalFormFillSessionService {
    public static ExternalFormFillSession createExternalFormFillSession(
            ExternalFormFillSessionsApi externalFormFillSessionsApi,
            String accountId,
            String formId,
            Integer roomId
    ) throws ApiException {
        // Step 3. Construct your request body
        ExternalFormFillSessionForCreate externalFormFillSessionForCreate = new ExternalFormFillSessionForCreate()
                .formId(formId)
                .roomId(roomId);

        // Step 4. Call the v2 Rooms API
        return externalFormFillSessionsApi.createExternalFormFillSession(
                accountId, externalFormFillSessionForCreate
        );
    }
}
