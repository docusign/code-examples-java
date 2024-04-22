package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.AddingFormsToRoomService;
import com.docusign.controller.rooms.services.GetFormSummaryListService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.api.RoomsApi.GetRoomsOptions;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormSummary;
import com.docusign.rooms.model.RoomDocument;
import com.docusign.rooms.model.RoomSummaryList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Adding forms to a room.
 */
@Controller
@RequestMapping("/r004")
public class R004ControllerAddingFormsToRoom extends AbstractRoomsController {

    private static final String MODEL_FORM_LIST = "formList";

    private static final String MODEL_ROOM_LIST = "roomList";

    public R004ControllerAddingFormsToRoom(DSConfiguration config, Session session, User user) {
        super(config, "r004");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3. Obtain the desired form ID
        //ds-snippet-start:Rooms4Step3
        GetRoomsOptions gro = roomsApi.new GetRoomsOptions();
        gro.setCount(5);
        RoomSummaryList roomSummaryList = roomsApi.getRooms(this.session.getAccountId(), gro);

        List<FormSummary> forms = GetFormSummaryListService.getFormSummaryList(
                createFormLibrariesApi(session.getBasePath(), this.user.getAccessToken()),
                this.session.getAccountId());

        //ds-snippet-end:Rooms4Step3
        model.addAttribute(MODEL_ROOM_LIST, roomSummaryList.getRooms());
        model.addAttribute(MODEL_FORM_LIST, forms);
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        // Step 2. Construct your API headers
        //ds-snippet-start:Rooms4Step2
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        //ds-snippet-end:Rooms4Step2

        // Step 4. Call the v2 Rooms API
        //ds-snippet-start:Rooms4Step4
        RoomDocument roomDocument = AddingFormsToRoomService.addFormsToRoom(
                roomsApi,
                this.session.getAccountId(),
                args.getFormId(),
                args.getRoomId());
        //ds-snippet-end:Rooms4Step4

        DoneExample.createDefault(this.title)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(roomDocument)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
