package com.docusign.controller.maestro.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.TriggerInputs;
import com.docusign.iam.sdk.models.components.TriggerWorkflow;
import com.docusign.iam.sdk.models.operations.*;

import java.util.HashMap;
import java.util.Map;

public class MaestroMethodsService {
    private static IamClient createAuthenticatedClient(String accessToken) {
        return IamClient.builder()
                .accessToken(accessToken)
                .build();
    }

    private static TriggerInputs createTriggerInput(String value) {
        return TriggerInputs.of(value);
    }

    public static TriggerWorkflowResponse triggerWorkflowInstance(
            String accessToken,
            String accountId,
            String workflowId,
            String signerEmail,
            String signerName,
            String ccEmail,
            String ccName,
            String instanceName) throws Exception {

        IamClient client = createAuthenticatedClient(accessToken);

        Map<String, TriggerInputs> triggerInputs = new HashMap<>();
        triggerInputs.put("signerName", createTriggerInput(signerName));
        triggerInputs.put("signerEmail", createTriggerInput(signerEmail));
        triggerInputs.put("ccName", createTriggerInput(ccName));
        triggerInputs.put("ccEmail", createTriggerInput(ccEmail));

        TriggerWorkflow triggerWorkflow = new TriggerWorkflow(instanceName, triggerInputs);

        return client.maestro()
                .workflows()
                .triggerWorkflow(accountId, workflowId, triggerWorkflow);
    }

    public static PauseNewWorkflowInstancesResponse PauseMaestroWorkflow(
            String accessToken,
            String accountId,
            String workflowId) throws Exception {
        var client = createAuthenticatedClient(accessToken);
        return client.maestro()
                .workflows().pauseNewWorkflowInstances(accountId, workflowId);
    }

    public static ResumePausedWorkflowResponse ResumeMaestroWorkflow(
            String accessToken,
            String accountId,
            String workflowId) throws Exception {
        var client = createAuthenticatedClient(accessToken);
        return client.maestro()
                .workflows().resumePausedWorkflow(accountId, workflowId);
    }

    public static CancelWorkflowInstanceResponse CancelMaestroWorkflowInstance(
            String accessToken,
            String accountId,
            String workflowId,
            String instanceId) throws Exception {
        var client = createAuthenticatedClient(accessToken);
        return client.maestro().workflowInstanceManagement()
                .cancelWorkflowInstance(accountId, workflowId, instanceId);
    }

    public static GetWorkflowsListResponse getMaestroWorkflow(
            String accessToken,
            String accountId) throws Exception {

        IamClient client = createAuthenticatedClient(accessToken);
        return client.maestro()
                .workflows()
                .getWorkflowsList(accountId);
    }
}
