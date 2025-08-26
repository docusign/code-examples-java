package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.maestro.services.TriggerWorkflowService;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * This example demonstrates how to trigger maestro workflow.
 */
@Controller
@RequestMapping("/mae001")
public class Mae001TriggerWorkflowController extends AbstractMaestroController {
    public static final String EMBED = "pages/maestro/examples/embed";

    private static final String MAESTRO_WORKFLOW_CONFIG = "maestro-workflow-definition.json";

    public static final String URL = "url";

    private static final String TEMPLATE_ID = "templateId";

    private static final String WORKFLOW_ID = "workflowId";

    private static final String PUBLISH_LINK = "publishLink";

    public static final String WORKFLOW_NAME = "Example workflow - send invite to signer";

    public static final String STATUS = "active";

    public Mae001TriggerWorkflowController(DSConfiguration config, Session session, User user) {
        super(config, "mae001", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        try {
            ensureWorkflowExists(model);
        } catch(Exception exception) {
                throw new Exception(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage);
        }

        model.addAttribute(TEMPLATE_ID, session.getTemplateId());
        model.addAttribute(WORKFLOW_ID, session.getWorkflowId());
    }

    @Override
    protected Object doWork(
            WorkArguments args,
            ModelMap model,
            HttpServletResponse response
    ) throws Exception{
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();
        var client = createAuthenticatedClient(accessToken);

        var triggerResponse = TriggerWorkflowService.triggerWorkflowInstance(
                client,
                accountId,
                session.getWorkflowId(),
                args.getSignerEmail(),
                args.getSignerName(),
                args.getCcEmail(),
                args.getCcName(),
                args.getInstanceName());

        triggerResponse.triggerWorkflowSuccess().ifPresent(success -> {
            session.setInstanceId(success.instanceId().orElse(null));
            model.addAttribute(URL, success.instanceUrl().orElse(""));
        });

        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute("example", this.getTextForCodeExample());

        return EMBED;
    }

    private void ensureWorkflowExists(ModelMap model) throws Exception {
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();
        var templateId = session.getTemplateId();

        findActiveWorkflow(accessToken, accountId)
                .ifPresent(workflowId -> session.setWorkflowId(workflowId));

        if (session.getIsWorkflowPublished()) {
            publishAndSetLink(model, accessToken, accountId, session.getWorkflowId());
        }

        if (session.getWorkflowId() == null && templateId != null) {
            var workflowId = TriggerWorkflowService.createWorkflow(accessToken, accountId, templateId, MAESTRO_WORKFLOW_CONFIG);

            session.setWorkflowId(workflowId);
            session.setIsWorkflowPublished(true);

            publishAndSetLink(model, accessToken, accountId, workflowId);
        }
    }

    private Optional<String> findActiveWorkflow(String accessToken, String accountId) throws Exception {
        var client = createAuthenticatedClient(accessToken);
        var workflowsResponse = TriggerWorkflowService.getMaestroWorkflow(client, accountId);

        return workflowsResponse.workflowsListSuccess().stream()
                .filter(w -> w.data().isPresent())
                .flatMap(w -> w.data().orElseThrow().stream())
                .filter(workflow ->
                        workflow.name().orElseThrow().equals(WORKFLOW_NAME) && workflow.status().orElseThrow().equals(STATUS))
                .map(workflow -> workflow.id().orElse(null))
                .findFirst();
    }

    private void publishAndSetLink(ModelMap model, String accessToken, String accountId, String workflowId) throws Exception {
        String publishLink = TriggerWorkflowService.publishWorkflow(accessToken, accountId, workflowId);

        if (!publishLink.isEmpty()) {
            String linkText = getTextForCodeExampleByApiType()
                    .getAdditionalPage()
                    .get(0)
                    .getResultsPageText()
                    .replaceFirst("\\{0}", publishLink);

            model.addAttribute(PUBLISH_LINK, linkText);
        } else {
            session.setIsWorkflowPublished(false);
        }
    }
}
