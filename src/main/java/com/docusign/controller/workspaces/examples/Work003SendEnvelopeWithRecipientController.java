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
        var accessToken = user.getAccessToken();
        var basePath = session.getBasePath();
        var workspaceId = session.getWorkspaceId();
        var documentId = session.getDocumentId();
        var signerEmail = args.getSignerEmail();
        var signerName = args.getSignerName();

        var envelope = SendEnvelopeWithRecipientInfoService.createWorkspaceEnvelope(accessToken, accountId, workspaceId, documentId);
        var envelopeId = envelope.createWorkspaceEnvelopeResponse().get().envelopeId().toString();
        var results = SendEnvelopeWithRecipientInfoService.sendEnvelope(accessToken, basePath, accountId, envelopeId, signerEmail, signerName);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
            .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
            .withJsonObject(results)
            .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
