package com.docusign.controller.maestro.services;

import com.docusign.maestro.api.WorkflowInstanceManagementApi;
import com.docusign.maestro.client.ApiClient;
import com.docusign.maestro.client.ApiException;
import com.docusign.maestro.model.*;

public final class GetWorkflowInstanceService {
    //ds-snippet-start:Maestro3Step3
    public static WorkflowInstance getWorkflowInstance(
            ApiClient apiClient,
            String accountId,
            String workflowId,
            String instanceId
    ) throws ApiException {
        var maestroApi = new WorkflowInstanceManagementApi(apiClient);
        return maestroApi.getWorkflowInstance(accountId, workflowId, instanceId);
    }
    //ds-snippet-end:Maestro3Step3
}
