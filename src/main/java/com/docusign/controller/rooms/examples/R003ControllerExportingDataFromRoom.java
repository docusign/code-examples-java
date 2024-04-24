package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.ExportingDataFromRoomService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FieldData;
import com.docusign.rooms.model.RoomSummaryList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Exporting data from a room.
 */
@Controller
@RequestMapping("/r003")
public class R003ControllerExportingDataFromRoom extends AbstractRoomsController {

    private static final String MODEL_ROOM_LIST = "roomList";

    public R003ControllerExportingDataFromRoom(DSConfiguration config, Session session, User user) {
        super(config, "r003");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        RoomSummaryList roomSummaryList = roomsApi.getRooms(this.session.getAccountId());

        model.addAttribute(MODEL_ROOM_LIST, roomSummaryList.getRooms());
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        // Step 2: Construct your API headers
        //ds-snippet-start:Rooms3Step2
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        //ds-snippet-end:Rooms3Step2

        // Step 3: Call the v2 Rooms API
        //ds-snippet-start:Rooms3Step3
        FieldData fieldData = ExportingDataFromRoomService.exportDataFromRoom(
                roomsApi,
                this.session.getAccountId(),
                args.getRoomId());
        //ds-snippet-end:Rooms3Step3

        DoneExample.createDefault(this.title)
                .withJsonObject(fieldData)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
