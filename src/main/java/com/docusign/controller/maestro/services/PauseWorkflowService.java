package com.docusign.controller.maestro.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.operations.*;

public class PauseWorkflowService {
    public static PauseNewWorkflowInstancesResponse PauseMaestroWorkflow(
            IamClient client,
            String accountId,
            String workflowId) throws Exception {
        return client.maestro()
                .workflows().pauseNewWorkflowInstances(accountId, workflowId);
    }
}
