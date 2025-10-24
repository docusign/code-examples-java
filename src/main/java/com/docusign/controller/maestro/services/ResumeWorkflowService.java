package com.docusign.controller.maestro.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.operations.*;

public class ResumeWorkflowService {
    public static ResumePausedWorkflowResponse ResumeMaestroWorkflow(
            IamClient client,
            String accountId,
            String workflowId) throws Exception {
        return client.maestro()
                .workflows().resumePausedWorkflow(accountId, workflowId);
    }
}
