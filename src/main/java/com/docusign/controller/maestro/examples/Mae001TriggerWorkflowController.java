package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.maestro.services.MaestroMethodsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to get all agreements.
 */
@Controller
@RequestMapping("/mae001")
public class Mae001TriggerWorkflowController extends AbstractMaestroController {
    private static final String MODEL_TEMPLATE_ID = "templateId";

    private static final String MODEL_WORKFLOW_ID = "workflowId";

    private static final String MODEL_PUBLISH_LINK_ID = "publishLink";

    public static final String WORKFLOW_NAME = "Example workflow - send invite to signer";

    public Mae001TriggerWorkflowController(DSConfiguration config, Session session, User user) {
        super(config, "mae001", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        //ApiClient apiClient = createApiClient(config.getMaestroBasePath(), user.getAccessToken());
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();
        String templateId = session.getTemplateId();

        try {
            var workflowDefinition = MaestroMethodsService.getMaestroWorkflow(
                    accessToken,
                    accountId);

            if (workflowDefinition.workflowsListSuccess() != null && workflowDefinition.workflowsListSuccess().isPresent()) {
                workflowDefinition.workflowsListSuccess().stream()
                        .filter(w -> w.data().isPresent()) // ensure data() is present
                        .flatMap(w -> w.data().get().stream()) // stream each workflow in the list
                        .filter(w -> w.name().get().equals(WORKFLOW_NAME)) // match by name
                        .findFirst()
                        .ifPresent(w -> session.setWorkflowId(w.id().get()));
            }

            var paused = MaestroMethodsService.PauseMaestroWorkflow(
                    accessToken,
                    accountId,
                    session.getWorkflowId());

            var resumed = MaestroMethodsService.ResumeMaestroWorkflow(
                    accessToken,
                    accountId,
                    session.getWorkflowId());

            var result = MaestroMethodsService.triggerWorkflowInstance(
                    accessToken,
                    accountId,
                    session.getWorkflowId(),
                    "a@gmail.com",
                    "anna",
                    "an@gmail.com",
                    "as",
                    "inst");

            /*
            if (session.getIsWorkflowPublished()) {
                String publishLink = TriggerWorkflowService.publishWorkFlow(
                        apiClient,
                        accountId,
                        session.getWorkflowId());

                if (!publishLink.isEmpty()) {
                    model.addAttribute(MODEL_PUBLISH_LINK_ID, getTextForCodeExampleByApiType().getAdditionalPage()
                            .get(0).getResultsPageText().replaceFirst("\\{0}", publishLink));
                } else {
                    session.setIsWorkflowPublished(false);
                }
            }

            if (session.getWorkflowId() == null && templateId != null) {
                WorkflowManagementApi managementApi = new WorkflowManagementApi(apiClient);

                NewOrUpdatedWorkflowDefinitionResponse createWorkflow = CreateWorkflowService.createWorkflowDefinition(
                        managementApi,
                        accountId,
                        templateId);

                session.setWorkflowId(createWorkflow.getWorkflowDefinitionId());

                String publishLink = TriggerWorkflowService.publishWorkFlow(
                        apiClient,
                        accountId,
                        session.getWorkflowId());

                session.setIsWorkflowPublished(true);
                model.addAttribute(MODEL_PUBLISH_LINK_ID, getTextForCodeExampleByApiType().getAdditionalPage().get(0)
                        .getResultsPageText().replaceFirst("\\{0}", publishLink));
            }

             */
        } catch(Exception exception) {
            System.out.println(exception.toString());
            exception.printStackTrace();
                throw exception;
        }

        model.addAttribute(MODEL_TEMPLATE_ID, templateId);
        model.addAttribute(MODEL_WORKFLOW_ID, session.getWorkflowId());
    }

    @Override
    protected Object doWork(
            WorkArguments args,
            ModelMap model,
            HttpServletResponse response
    ) throws Exception{
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();

        var workflowDefinition = MaestroMethodsService.PauseMaestroWorkflow(
                accessToken,
                accountId,
                session.getWorkflowId());

        var r = MaestroMethodsService.ResumeMaestroWorkflow(
                accessToken,
                accountId,
                session.getWorkflowId());

        var result = MaestroMethodsService.triggerWorkflowInstance(
                accessToken,
                accountId,
                session.getWorkflowId(),
                args.getSignerEmail(),
                args.getSignerName(),
                args.getCcEmail(),
                args.getCcName(),
                args.getInstanceName());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(result)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
