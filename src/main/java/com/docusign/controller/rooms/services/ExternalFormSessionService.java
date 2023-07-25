package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.ExternalFormFillSessionsApi;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.ExternalFormFillSession;
import com.docusign.rooms.model.ExternalFormFillSessionForCreate;
import com.docusign.rooms.model.RoomDocumentList;
import com.docusign.rooms.model.RoomSummaryList;

public class ExternalFormSessionService {
    public RoomDocumentList getDocuments(
            RoomsApi roomsApi,
            String accountId,
            Integer roomId
    ) throws ApiException {
        return roomsApi.getDocuments(
                accountId,
                roomId);
    }

    public RoomSummaryList getRooms(
            RoomsApi roomsApi,
            String accountId
    ) throws ApiException {
        return roomsApi.getRooms(accountId);
    }

    public ExternalFormFillSession createSession(
            ExternalFormFillSessionsApi externalFormFillSessionsApi,
            String accountId,
            ExternalFormFillSessionForCreate sessionForCreate
    ) throws ApiException {
        return externalFormFillSessionsApi.createExternalFormFillSession(accountId, sessionForCreate);
    }
}
