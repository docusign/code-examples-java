package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.CreateRoomService;
import com.docusign.controller.rooms.services.GetAdminRolesService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.RoleSummary;
import com.docusign.rooms.model.Room;
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

    public R001ControllerCreateRoom(DSConfiguration config, Session session, User user) {
        super(config, "r001");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {

        RoleSummary adminRole = GetAdminRolesService.getAdminRole(
                createRolesApiClient(this.session.getBasePath(), this.user.getAccessToken()),
                this.session.getAccountId());

        // Step 2: Construct your API headers
        //ds-snippet-start:Rooms1Step2
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        //ds-snippet-end:Rooms1Step2

        // Step 3: Call the v2 Rooms API
        //ds-snippet-start:Rooms1Step3
        Room createdRoom = CreateRoomService.createRoomFromModel(
                roomsApi,
                this.session.getAccountId(),
                CreateRoomService.createRoomModel(adminRole.getRoleId(), args.getRoomName()));
        //ds-snippet-end:Rooms1Step3

        DoneExample.createDefault(this.title)
                .withJsonObject(createdRoom)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", createdRoom.getName())
                        .replaceFirst("\\{1}", createdRoom.getRoomId().toString())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
