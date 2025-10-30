package com.docusign.controller.maestro.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.operations.*;

public class ResumeWorkflowService {
    //ds-snippet-start:Maestro3Step3
    public static ResumePausedWorkflowResponse ResumeMaestroWorkflow(
            IamClient client,
            String accountId,
            String workflowId) throws Exception {
        return client.maestro()
                .workflows().resumePausedWorkflow(accountId, workflowId);
    }
    //ds-snippet-end:Maestro3Step3
}
