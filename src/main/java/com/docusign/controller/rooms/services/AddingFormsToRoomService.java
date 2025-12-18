package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
import com.docusign.rooms.model.FormForAdd;
import com.docusign.rooms.model.RoomDocument;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class AddingFormsToRoomService {
    public static RoomDocument addFormsToRoom(
            RoomsApi roomsApi,
            String accountId,
            UUID formId,
            Integer roomId
    ) throws ApiException {
        FormForAdd formForAdd = new FormForAdd().formId(formId);
        ApiResponse<RoomDocument> response = roomsApi.addFormToRoomWithHttpInfo(accountId, roomId, formForAdd);

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
