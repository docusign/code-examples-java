package com.docusign.controller.workspaces.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.workspaces.services.CreateWorkspaceService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to create a new Workspace.
 */
@Controller
@RequestMapping("/work001")
public class Work001CreateWorkspaceController extends AbstractWorkspacesController {

    public Work001CreateWorkspaceController(DSConfiguration config, Session session, User user) {
        super(config, "work001", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        var accountId = session.getAccountId();

        //ds-snippet-start:Workspaces1Step2
        var accessToken = user.getAccessToken();
        //ds-snippet-end:Workspaces1Step2
        
        //ds-snippet-start:Workspaces1Step3
        var workspaceName = args.getWorkspaceName();
        //ds-snippet-end:Workspaces1Step3

       //ds-snippet-start:Workspaces1Step4
        var workspace = CreateWorkspaceService.createWorkspace(accessToken, accountId, workspaceName);
        //ds-snippet-end:Workspaces1Step4
        
        var workspaceId = workspace.createWorkspaceResponse().get().workspaceId().orElse("");
        var creatorId = workspace.createWorkspaceResponse().get().createdByUserId().orElse("");
        session.setWorkspaceId(workspaceId);
        session.setCreatorId(creatorId);
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
            .withMessage(getTextForCodeExampleByApiType().ResultsPageText.replaceFirst("\\{0}", workspaceId))
            .withJsonObject(workspace.createWorkspaceResponse().get())
            .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
