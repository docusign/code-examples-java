package com.docusign.controller.admin.services;

import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.model.OrganizationExportResponse;

import java.util.UUID;

public class CheckRequestStatusService {
    public static OrganizationExportResponse checkRequestStatus(
            BulkExportsApi bulkExportsApi,
            UUID organizationId,
            UUID exportId
    ) throws Exception {
        return bulkExportsApi.getUserListExport(organizationId, exportId);
    }
}
