package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.OfficesApi;
import com.docusign.rooms.api.RolesApi;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a room.
 */
@Controller
@RequestMapping("/r001")
public class R001ControllerCreateRoom extends AbstractRoomsController {

    private final Session session;
    private final User user;


    @Autowired
    public R001ControllerCreateRoom(DSConfiguration config, Session session, User user) {
        super(config, "r001", "Create a room");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        RoleSummary adminRole = getAdminRole(this.session.getBasePath(), this.user.getAccessToken(), this.session.getAccountId());

        // Step 2: Construct your API headers
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3: Construct the request body for your room
        RoomForCreate roomForCreate = this.createRoom(adminRole.getRoleId(), args.getRoomName());
        String accountId = this.session.getAccountId();

        // Step 4: Call the v2 Rooms API
        Room createdRoom = roomsApi.createRoom(accountId, roomForCreate);

        DoneExample.createDefault(this.title)
                .withJsonObject(createdRoom)
                .withMessage("The room has been created!<br />Room ID " + createdRoom.getRoomId() + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    private RoomForCreate createRoom(Integer roleId, String roomName) {
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
                                .putDataItem("comments", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                );
    }
    // ***DS.snippet.0.end
}
