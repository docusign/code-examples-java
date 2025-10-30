package com.docusign.controller.maestro.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.operations.*;

public class PauseWorkflowService {
    //ds-snippet-start:Maestro2Step3
    public static PauseNewWorkflowInstancesResponse PauseMaestroWorkflow(
            IamClient client,
            String accountId,
            String workflowId) throws Exception {
        return client.maestro()
                .workflows().pauseNewWorkflowInstances(accountId, workflowId);
    }
    //ds-snippet-end:Maestro2Step3
}
