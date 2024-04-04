package com.docusign.controller.maestro.services;

import com.docusign.maestro.api.WorkflowInstanceManagementApi;
import com.docusign.maestro.client.ApiClient;
import com.docusign.maestro.client.ApiException;
import com.docusign.maestro.model.CancelResponse;

public final class CancelWorkflowInstanceService {
    public static CancelResponse cancelWorkflowInstance(
            ApiClient apiClient,
            String accountId,
            String instanceId
    ) throws ApiException {
        var maestroApi = new WorkflowInstanceManagementApi(apiClient);
        return maestroApi.cancelWorkflowInstance(accountId, instanceId);
    }
}
