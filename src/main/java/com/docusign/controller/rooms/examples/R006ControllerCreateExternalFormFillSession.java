package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.ExternalFormSessionService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.ExternalFormFillSessionsApi;
import com.docusign.rooms.api.RoomsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.ExternalFormFillSession;
import com.docusign.rooms.model.ExternalFormFillSessionForCreate;
import com.docusign.rooms.model.RoomDocumentList;
import com.docusign.rooms.model.RoomSummaryList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Creating an external form fill session.
 */
@Controller
@RequestMapping("/r006")
public class R006ControllerCreateExternalFormFillSession extends AbstractRoomsController {

    private static final String MODEL_ROOMS_LIST = "roomsList";

    private static final String MODEL_DOCUMENTS_LIST = "documentsList";

    public R006ControllerCreateExternalFormFillSession(DSConfiguration config, Session session, User user) {
        super(config, "r006");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        RoomsApi roomsApi = createRoomsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        RoomSummaryList rooms = (new ExternalFormSessionService()).getRooms(roomsApi, this.session.getAccountId());

        Integer roomsId = args.getRoomId();

        if (roomsId != null) {
            RoomDocumentList documents = (new ExternalFormSessionService()).getDocuments(roomsApi, this.session.getAccountId(), roomsId);
            model.addAttribute(MODEL_DOCUMENTS_LIST, documents.getDocuments());
        }

        model.addAttribute(MODEL_ROOMS_LIST, rooms.getRooms());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        ExternalFormFillSessionsApi externalFormFillSessionsApi = createExternalFormFillSessionsApiClient(
                this.session.getBasePath(), this.user.getAccessToken());

        //ds-snippet-start:Rooms6Step3
        ExternalFormFillSessionForCreate sessionToCreate = new ExternalFormFillSessionForCreate();
        sessionToCreate.setRoomId(args.getRoomId());
        sessionToCreate.setFormId(args.getDocumentId());
        sessionToCreate.setXFrameAllowedUrl("http://localhost:8080");
        //ds-snippet-end:Rooms6Step3

        //ds-snippet-start:Rooms6Step4
        ExternalFormFillSession formFillSession = (new ExternalFormSessionService()).createSession(
                externalFormFillSessionsApi,
                this.session.getAccountId(),
                sessionToCreate);
        //ds-snippet-end:Rooms6Step4
        //ds-snippet-start:Rooms6Step5
        DoneExample.createDefault(this.title)
                .withMessage(getTextForCodeExample().ResultsPageText)
                .withJsonObject(formFillSession)
                .withFormFill(formFillSession.getUrl())
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
        //ds-snippet-end:Rooms6Step5
    }
    // ***DS.snippet.0.end
}
