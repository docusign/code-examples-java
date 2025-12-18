package com.docusign.controller.rooms.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.rooms.api.ExternalFormFillSessionsApi;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.api.RoomsApi.GetDocumentsOptions;
import com.docusign.rooms.api.RoomsApi.GetRoomsOptions;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
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
        ApiResponse<RoomDocumentList> response = roomsApi.getDocumentsWithHttpInfo(
                accountId,
                roomId,
            (GetDocumentsOptions)null);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }

    public RoomSummaryList getRooms(
            RoomsApi roomsApi,
            String accountId
    ) throws ApiException {
        ApiResponse<RoomSummaryList> response = roomsApi.getRoomsWithHttpInfo(accountId, (GetRoomsOptions)null);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }

    public ExternalFormFillSession createSession(
            ExternalFormFillSessionsApi externalFormFillSessionsApi,
            String accountId,
            ExternalFormFillSessionForCreate sessionForCreate
    ) throws ApiException {
        ApiResponse<ExternalFormFillSession> response = externalFormFillSessionsApi.createExternalFormFillSessionWithHttpInfo(accountId, sessionForCreate);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
