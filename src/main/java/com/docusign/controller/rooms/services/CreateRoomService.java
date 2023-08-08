package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FieldDataForCreate;
import com.docusign.rooms.model.Room;
import com.docusign.rooms.model.RoomForCreate;

//ds-snippet-start:Rooms1Step4
public final class CreateRoomService {
    public static Room createRoomFromModel(
            RoomsApi roomsApi,
            String accountId,
            RoomForCreate roomForCreate
    ) throws ApiException {
        return roomsApi.createRoom(accountId, roomForCreate);
    }
//ds-snippet-end:Rooms1Step4

    public static RoomForCreate createRoomModel(Integer roleId, String roomName) {
        return new RoomForCreate()
                .name(roomName)
                .roleId(roleId)
                .transactionSideId("listbuy")
                .fieldData(
                        new FieldDataForCreate()
                                .putDataItem("address1", "123 EZ Street")
                                .putDataItem("address2", "unit 10")
                                .putDataItem("city", "Galaxian")
                                .putDataItem("state", "US-HI")
                                .putDataItem("postalCode", "11112")
                                .putDataItem("companyRoomStatus", "5")
                                .putDataItem(
                                        "comments",
                                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                );
    }
}
