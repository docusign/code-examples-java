package com.docusign.controller.rooms.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
import com.docusign.rooms.model.RoomSummaryList;

public final class GetRoomsWithFiltersService {
    public static RoomSummaryList getRoomsWithFilters(
            RoomsApi roomsApi,
            String accountId,
            String startDate,
            String endDate) throws ApiException {
        // Step 3. Prepare your request parameters
        // ds-snippet-start:Rooms5Step3
        RoomsApi.GetRoomsOptions options = roomsApi.new GetRoomsOptions();
        options.setFieldDataChangedStartDate(startDate);
        options.setFieldDataChangedEndDate(endDate);
        // ds-snippet-end:Rooms5Step3

        // Call the v2 Rooms API
        ApiResponse<RoomSummaryList> response = roomsApi.getRoomsWithHttpInfo(accountId, options);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
