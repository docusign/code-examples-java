package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.maestro.services.PauseWorkflowService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to pause workflow.
 */
@Controller
@RequestMapping("/mae002")
public class Mae002PauseWorkflowController extends AbstractMaestroController {

    private static final String MODEL_IS_WORKFLOW_ID_PRESENT = "isWorkflowIdPresent";

    public static final String NO_WORKFLOW_AVAILABLE_TO_PAUSE = "No workflow available to pause.";

    public Mae002PauseWorkflowController(DSConfiguration config, Session session, User user) {
        super(config, "mae002", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_IS_WORKFLOW_ID_PRESENT, session.getWorkflowId() != null );
    }

    @Override
    protected Object doWork(
            WorkArguments args,
            ModelMap model,
            HttpServletResponse response
    ) throws Exception{
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();
        var workflowId = session.getWorkflowId();
        var client = createAuthenticatedClient(accessToken);

        if (workflowId == null) {
            throw new IllegalStateException(NO_WORKFLOW_AVAILABLE_TO_PAUSE);
        }

        var paused = PauseWorkflowService.PauseMaestroWorkflow(
                client,
                accountId,
                workflowId);
        var jsonResponse = serializeObjectToJson(paused.pauseNewWorkflowInstancesSuccess().orElseThrow());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(jsonResponse)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
