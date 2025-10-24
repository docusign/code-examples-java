package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.maestro.services.CancelWorkflowInstanceService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to cancel workflow instance.
 */
@Controller
@RequestMapping("/mae004")
public class Mae004CancelWorkflowInstanceController extends AbstractMaestroController {
    private static final String MODEL_IS_WORKFLOW_ID_PRESENT = "isWorkflowIdPresent";

    public static final String NO_WORKFLOW_INSTANCE_AVAILABLE_TO_CANCEL = "No workflow instance available to cancel.";

    public Mae004CancelWorkflowInstanceController(DSConfiguration config, Session session, User user) {
        super(config, "mae004", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_IS_WORKFLOW_ID_PRESENT, session.getWorkflowId() != null && session.getInstanceId() != null );
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
        var instanceId = session.getInstanceId();
        var client = createAuthenticatedClient(accessToken);

        if (workflowId == null || instanceId == null) {
            throw new IllegalStateException(NO_WORKFLOW_INSTANCE_AVAILABLE_TO_CANCEL);
        }

        var paused = CancelWorkflowInstanceService.CancelMaestroWorkflowInstance(
                client,
                accountId,
                workflowId,
                instanceId);

        var jsonResponse = serializeObjectToJson(paused.cancelWorkflowInstanceResponse().orElseThrow());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(jsonResponse)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
