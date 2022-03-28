package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FieldData;

public final class ExportingDataFromRoomService {
    public static FieldData exportDataFromRoom(
            RoomsApi roomsApi,
            String accountId,
            Integer roomId
    ) throws ApiException {
        return roomsApi.getRoomFieldData(accountId, roomId);
    }
}
