package com.docusign.controller.admin.services;

import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.OrganizationExportResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckRequestStatusService {
    public static OrganizationExportResponse checkRequestStatus(
            BulkExportsApi bulkExportsApi,
            UUID organizationId,
            UUID exportId) throws Exception {
        ApiResponse<OrganizationExportResponse> response = bulkExportsApi.getUserListExportWithHttpInfo(organizationId,
                exportId);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
