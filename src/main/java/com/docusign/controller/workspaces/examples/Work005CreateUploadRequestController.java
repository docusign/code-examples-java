package com.docusign.controller.workspaces.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.workspaces.services.CreateUploadRequestService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to create upload request.
 */
@Controller
@RequestMapping("/work005")
public class Work005CreateUploadRequestController extends AbstractWorkspacesController {
    private static final String MODEL_WORKSPACE_OK = "workspaceOk";

    private static final String MODEL_CREATOR_OK = "creatorOk";

    public Work005CreateUploadRequestController(DSConfiguration config, Session session, User user) {
        super(config, "work005", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        model.addAttribute(MODEL_WORKSPACE_OK, session.getWorkspaceId() != null && !session.getWorkspaceId().isEmpty());
        model.addAttribute(MODEL_CREATOR_OK, session.getCreatorId() != null && !session.getCreatorId().isEmpty());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();
        var workspaceId = session.getWorkspaceId();
        var creatorId = session.getCreatorId();
        var assigneeEmail = args.getAssigneeEmail();

        try{
            var results = CreateUploadRequestService.createUploadRequest(
                    accessToken,
                    accountId,
                    workspaceId,
                    creatorId,
                    assigneeEmail);

            DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                    .withMessage(getTextForCodeExampleByApiType().ResultsPageText.replaceFirst(
                            "\\{0}",
                            results.getWorkspaceUploadRequestResponse().get().uploadRequestId()))
                    .addToModel(model, config);
        } catch (Exception e) {
            DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                    .withMessage(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage)
                    .addToModel(model, config);
        }

        return DONE_EXAMPLE_PAGE;
    }
}
