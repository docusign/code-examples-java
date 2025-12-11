package com.docusign.controller.workspaces.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.workspaces.services.AddDocumentToWorkspaceService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to add a Document to the Workspace.
 */
@Controller
@RequestMapping("/work002")
public class Work002AddDocumentToWorkspaceController extends AbstractWorkspacesController {
    private static final String MODEL_WORKSPACE_OK = "workspaceOk";

    private static final String MODEL_DOCUMENT_FOLDER = "documentFolder";

    public Work002AddDocumentToWorkspaceController(DSConfiguration config, Session session, User user) {
        super(config, "work002", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        model.addAttribute(MODEL_WORKSPACE_OK, session.getWorkspaceId() != null && !session.getWorkspaceId().isEmpty());
        model.addAttribute(MODEL_DOCUMENT_FOLDER, config.getCodeExamplesText().SupportingTexts.HelpingTexts.SelectPDFFileFromFolder.replaceFirst("\\{0}", Paths.get("src", "main", "resources").toAbsolutePath().toString()));
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        var accountId = session.getAccountId();

        //ds-snippet-start:Workspaces2Step2
        var accessToken = user.getAccessToken();
        //ds-snippet-end:Workspaces2Step2

        var workspaceId = session.getWorkspaceId();

        //ds-snippet-start:Workspaces2Step3
        var documentName = args.getDocumentName();
        var documentPath = args.getDocumentPath();
        //ds-snippet-end:Workspaces2Step3

        //ds-snippet-start:Workspaces2Step4
        var results = AddDocumentToWorkspaceService.addDocumentToWorkspace(accessToken, accountId, workspaceId, documentPath, documentName);
        //ds-snippet-end:Workspaces2Step4

        var documentId = results.createWorkspaceDocumentResponse().get().documentId().orElse("");
        session.setDocumentId(documentId);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
            .withMessage(getTextForCodeExampleByApiType().ResultsPageText.replaceFirst("\\{0}", documentId))
            .withJsonObject(results.createWorkspaceDocumentResponse().get())
            .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
