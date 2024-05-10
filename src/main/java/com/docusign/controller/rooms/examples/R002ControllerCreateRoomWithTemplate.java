package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.CreateRoomWithTemplateService;
import com.docusign.controller.rooms.services.GetAdminRolesService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.RoomTemplatesApi;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.RoleSummary;
import com.docusign.rooms.model.Room;
import com.docusign.rooms.model.RoomTemplatesSummaryList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a room with template.
 */
@Controller
@RequestMapping("/r002")
public class R002ControllerCreateRoomWithTemplate extends AbstractRoomsController {

    private static final String MODEL_TEMPLATES_LIST = "templateList";

    public R002ControllerCreateRoomWithTemplate(DSConfiguration config, Session session, User user) {
        super(config, "r002");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        // Step 3: Retrieve a Rooms Template ID
        //ds-snippet-start:Rooms2Step3
        RoomTemplatesApi templatesApi = createRoomTemplatesApiClient(
                this.session.getBasePath(),
                this.user.getAccessToken());
        RoomTemplatesSummaryList templatesSummaryList = templatesApi.getRoomTemplates(this.session.getAccountId());
        //ds-snippet-end:Rooms2Step3
        model.addAttribute(MODEL_TEMPLATES_LIST, templatesSummaryList.getRoomTemplates());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        RoleSummary adminRole = GetAdminRolesService.getAdminRole(
                createRolesApiClient(this.session.getBasePath(), this.user.getAccessToken()),
                this.session.getAccountId());

        // Step 2: Construct your API headers
        //ds-snippet-start:Rooms2Step2
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        //ds-snippet-end:Rooms2Step2

        // Step 4: Call the v2 Rooms API
        //ds-snippet-start:Rooms2Step4
        Room createdRoom = CreateRoomWithTemplateService.createRoomFromModel(
                roomsApi,
                this.session.getAccountId(),
                CreateRoomWithTemplateService.createRoom(
                        adminRole.getRoleId(),
                        args.getRoomName(),
                        args.getRoomTemplateId()));
        //ds-snippet-end:Rooms2Step4
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
