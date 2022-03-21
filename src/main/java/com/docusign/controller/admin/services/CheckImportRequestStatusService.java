package com.docusign.controller.admin.services;

import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.model.OrganizationImportResponse;

import java.util.UUID;

public class CheckImportRequestStatusService {
    public static OrganizationImportResponse checkRequestStatus(
            BulkImportsApi bulkImportsApi,
            UUID organizationId,
            UUID importId
    ) throws Exception {
        // Step 4a start
        return bulkImportsApi.getBulkUserImportRequest(organizationId, importId);
        // Step 4a end
    }
}
