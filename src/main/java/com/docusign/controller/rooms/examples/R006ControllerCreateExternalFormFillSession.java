package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.ExternalFormFillSessionsApi;
import com.docusign.rooms.api.FormLibrariesApi;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.ExternalFormFillSession;
import com.docusign.rooms.model.ExternalFormFillSessionForCreate;
import com.docusign.rooms.model.FormSummary;
import com.docusign.rooms.model.RoomSummaryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Creating an external form fill session.
 */
@Controller
@RequestMapping("/r006")
public class R006ControllerCreateExternalFormFillSession extends AbstractRoomsController {

    private static final String MODEL_FORM_LIST = "formList";
    private static final String MODEL_ROOM_LIST = "roomList";

    private final Session session;
    private final User user;

    @Autowired
    public R006ControllerCreateExternalFormFillSession(DSConfiguration config, Session session, User user) {
        super(config, "r006", "Creating an external form fill session");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        RoomSummaryList roomSummaryList = roomsApi.getRooms(this.session.getAccountId());

        List<FormSummary> forms = getFormSummaryList(this.session.getBasePath(), this.user.getAccessToken(), this.session.getAccountId());

        model.addAttribute(MODEL_ROOM_LIST, roomSummaryList.getRooms());
        model.addAttribute(MODEL_FORM_LIST, forms);

    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        // Step 2. Construct your API headers
        ExternalFormFillSessionsApi externalFormFillSessionsApi = createExternalFormFillSessionsApiClient(
                this.session.getBasePath(), this.user.getAccessToken()
        );

        // Step 3. Construct your request body
        ExternalFormFillSessionForCreate externalFormFillSessionForCreate = new ExternalFormFillSessionForCreate()
                .formId(args.getFormId().toString())
                .roomId(args.getRoomId());

        // Step 4. Call the v2 Rooms API
        ExternalFormFillSession externalFormFillSession = externalFormFillSessionsApi.createExternalFormFillSession(
                this.session.getAccountId(), externalFormFillSessionForCreate
        );


        DoneExample.createDefault(this.title)
                .withJsonObject(externalFormFillSession)
                .withMessage("An external form fill session has been added!")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
