package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormForAdd;
import com.docusign.rooms.model.RoomDocument;

import java.util.UUID;

public final class AddingFormsToRoomService {
    public static RoomDocument addFormsToRoom(
            RoomsApi roomsApi,
            String accountId,
            UUID formId,
            Integer roomId
    ) throws ApiException {
        FormForAdd formForAdd = new FormForAdd().formId(formId);
        return roomsApi.addFormToRoom(accountId, roomId, formForAdd);
    }
}
