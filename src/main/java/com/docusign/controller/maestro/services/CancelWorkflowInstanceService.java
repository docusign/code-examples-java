package com.docusign.controller.maestro.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.operations.*;

public class CancelWorkflowInstanceService {
    public static CancelWorkflowInstanceResponse CancelMaestroWorkflowInstance(
            IamClient client,
            String accountId,
            String workflowId,
            String instanceId) throws Exception {
        return client.maestro().workflowInstanceManagement()
                .cancelWorkflowInstance(accountId, workflowId, instanceId);
    }
}
