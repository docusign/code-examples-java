package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.RoomSummaryList;

public final class GetRoomsWithFiltersService {
    public static RoomSummaryList getRoomsWithFilters(
            RoomsApi roomsApi,
            String accountId,
            String startDate,
            String endDate
    ) throws ApiException {
        // Step 3. Prepare your request parameters
        //ds-snippet-start:Rooms5Step3
        RoomsApi.GetRoomsOptions options = roomsApi.new GetRoomsOptions();
        options.setFieldDataChangedStartDate(startDate);
        options.setFieldDataChangedEndDate(endDate);
        //ds-snippet-end:Rooms5Step3

        //Call the v2 Rooms API
        return roomsApi.getRooms(accountId, options);
    }
}
