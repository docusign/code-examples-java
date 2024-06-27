package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.maestro.services.CreateWorkflowService;
import com.docusign.controller.maestro.services.TriggerWorkflowService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.maestro.client.ApiException;
import com.docusign.maestro.api.WorkflowManagementApi;
import com.docusign.maestro.client.ApiClient;
import com.docusign.maestro.model.NewOrUpdatedWorkflowDefinitionResponse;
import com.docusign.maestro.model.WorkflowDefinitionList;
import com.docusign.maestro.model.WorkflowDefinitionMetadata;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Controller
@RequestMapping("/mae001")
public class EG001ControllerTriggerWorkflow extends AbstractMaestroController {

    private static final String MODEL_TEMPLATE_ID = "templateId";

    private static final String MODEL_WORKFLOW_ID = "workflowId";

    private static final String MODEL_PUBLISH_LINK_ID = "publishLink";

    public static final String WORKFLOW_NAME = "Example workflow - send invite to signer";

    public EG001ControllerTriggerWorkflow(DSConfiguration config, Session session, User user) {
        super(config, "mae001", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        ApiClient apiClient = createApiClient(config.getMaestroBasePath(), user.getAccessToken());
        String accountId = session.getAccountId();
        String templateId = session.getTemplateId();

        try {
            WorkflowDefinitionList workflowDefinition = TriggerWorkflowService.getWorkflowDefinitions(
                    apiClient,
                    accountId);

            if (workflowDefinition.getValue() != null && !workflowDefinition.getValue().isEmpty()) {
                Optional<WorkflowDefinitionMetadata> workflow = workflowDefinition
                        .getValue()
                        .stream()
                        .filter(x -> x.getName().equals(WORKFLOW_NAME))
                        .min((x, y) -> y.getLastUpdatedDate().compareTo(x.getLastUpdatedDate()));

                workflow.ifPresent(workflowDefinitionMetadata -> session.setWorkflowId(workflowDefinitionMetadata.getId()));
            }

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
        } catch(ApiException exception) {
            if(exception.getCode() == 403) {
                model.addAttribute(MODEL_PUBLISH_LINK_ID, config.getCodeExamplesText().SupportingTexts.ContactSupportToEnableFeature
                        .replaceFirst("\\{0}", "Maestro"));
            } else {
                throw exception;
            }
        }

        model.addAttribute(MODEL_TEMPLATE_ID, templateId);
        model.addAttribute(MODEL_WORKFLOW_ID, session.getWorkflowId());
    }

    @Override
    protected Object doWork(
            WorkArguments args,
            ModelMap model,
            HttpServletResponse response
    ) throws ApiException, IOException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        //ds-snippet-start:Maestro1Step2
        ApiClient apiClient = createApiClient(config.getMaestroBasePath(), user.getAccessToken());
        //ds-snippet-end:Maestro1Step2
        String accountId = session.getAccountId();

        //ds-snippet-start:Maestro1Step3
        var workflow = TriggerWorkflowService.getWorkflowDefinition(
                apiClient,
                accountId,
                session.getWorkflowId());
        //ds-snippet-end:Maestro1Step3

        var result = TriggerWorkflowService.triggerWorkflow(
                apiClient,
                accountId,
                workflow.getTriggerUrl(),
                args.getSignerEmail(),
                args.getSignerName(),
                args.getCcEmail(),
                args.getCcName(),
                args.getInstanceName(),
                session.getWorkflowId());

        session.setInstanceId(result.getInstanceId());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(result)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
