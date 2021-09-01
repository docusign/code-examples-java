package com.docusign.services.rooms.examples;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.RoomSummaryList;

public final class GetRoomsWithFiltersService {
    public static RoomSummaryList GetRoomsWithFilters(
            RoomsApi roomsApi,
            String accountId,
            String startDate,
            String endDate) throws ApiException {
        // Step 3. Prepare your request parameters
        RoomsApi.GetRoomsOptions options = roomsApi.new GetRoomsOptions();
        options.setFieldDataChangedStartDate(startDate);
        options.setFieldDataChangedEndDate(endDate);

        // Step 4. Call the v2 Rooms API
        return roomsApi.getRooms(accountId, options);
    }
}
