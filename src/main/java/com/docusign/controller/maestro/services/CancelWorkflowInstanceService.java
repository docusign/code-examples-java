package com.docusign.controller.maestro.services;

import com.docusign.maestro.api.WorkflowInstanceManagementApi;
import com.docusign.maestro.client.ApiClient;
import com.docusign.maestro.client.ApiException;
import com.docusign.maestro.model.CancelResponse;

public final class CancelWorkflowInstanceService {
    //ds-snippet-start:Maestro2Step3
    public static CancelResponse cancelWorkflowInstance(
            ApiClient apiClient,
            String accountId,
            String instanceId
    ) throws ApiException {
        var maestroApi = new WorkflowInstanceManagementApi(apiClient);
        return maestroApi.cancelWorkflowInstance(accountId, instanceId);
    }
    //ds-snippet-end:Maestro2Step3
}
