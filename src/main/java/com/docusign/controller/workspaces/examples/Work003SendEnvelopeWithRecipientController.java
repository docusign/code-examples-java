package com.docusign.controller.workspaces.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.workspaces.services.SendEnvelopeWithRecipientInfoService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to create and send a Workspace Envelope.
 */
@Controller
@RequestMapping("/work003")
public class Work003SendEnvelopeWithRecipientController extends AbstractWorkspacesController {
    private static final String MODEL_WORKSPACE_OK = "workspaceOk";

    private static final String MODEL_DOCUMENT_OK = "documentOk";

    public Work003SendEnvelopeWithRecipientController(DSConfiguration config, Session session, User user) {
        super(config, "work003", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        model.addAttribute(MODEL_WORKSPACE_OK, session.getWorkspaceId() != null && !session.getWorkspaceId().isEmpty());
        model.addAttribute(MODEL_DOCUMENT_OK, session.getDocumentId() != null && !session.getDocumentId().isEmpty());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        var accountId = session.getAccountId();

         //ds-snippet-start:Workspaces3Step2
        var accessToken = user.getAccessToken();
        //ds-snippet-end:Workspaces3Step2

        var basePath = session.getBasePath();
        var workspaceId = session.getWorkspaceId();

        //ds-snippet-start:Workspaces3Step3
        var documentId = session.getDocumentId();
        //ds-snippet-end:Workspaces3Step3

        var signerEmail = args.getSignerEmail();
        var signerName = args.getSignerName();

        //ds-snippet-start:Workspaces3Step4
        var envelope = SendEnvelopeWithRecipientInfoService.createWorkspaceEnvelope(accessToken, accountId, workspaceId, documentId);
        //ds-snippet-end:Workspaces3Step4

        //ds-snippet-start:Workspaces3Step5
        var envelopeId = envelope.createWorkspaceEnvelopeResponse().get().envelopeId().get();
        //ds-snippet-end:Workspaces3Step5

        //ds-snippet-start:Workspaces3Step6
        var results = SendEnvelopeWithRecipientInfoService.sendEnvelope(accessToken, basePath, accountId, envelopeId, signerEmail, signerName);
        //ds-snippet-end:Workspaces3Step6

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
            .withMessage(getTextForCodeExampleByApiType().ResultsPageText.replaceFirst("\\{0}", envelopeId))
            .withJsonObject(results)
            .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
