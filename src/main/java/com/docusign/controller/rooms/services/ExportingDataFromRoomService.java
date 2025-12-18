package com.docusign.controller.rooms.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
import com.docusign.rooms.model.FieldData;

public final class ExportingDataFromRoomService {
    public static FieldData exportDataFromRoom(
            RoomsApi roomsApi,
            String accountId,
            Integer roomId
    ) throws ApiException {
        ApiResponse<FieldData> response = roomsApi.getRoomFieldDataWithHttpInfo(accountId, roomId);

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
