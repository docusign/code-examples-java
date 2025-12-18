package com.docusign.controller.admin.services;

import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.OrganizationImportResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckImportRequestStatusService {
    public static OrganizationImportResponse checkRequestStatus(
            BulkImportsApi bulkImportsApi,
            UUID organizationId,
            UUID importId
    ) throws Exception {
        // Step 4a start
        ApiResponse<OrganizationImportResponse> response = bulkImportsApi.getBulkUserImportRequestWithHttpInfo(organizationId, importId);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        // Step 4a end
    }
}
