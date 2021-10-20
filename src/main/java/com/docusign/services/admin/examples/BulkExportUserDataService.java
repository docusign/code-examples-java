package com.docusign.services.admin.examples;

import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.OrganizationExportRequest;
import com.docusign.admin.model.OrganizationExportResponse;
import com.docusign.admin.model.OrganizationExportsResponse;

import java.util.UUID;

public class BulkExportUserDataService {
    public static OrganizationExportsResponse bulkExportsUserData(
            BulkExportsApi bulkExportsApi,
            UUID organizationId) throws ApiException
    {
        return bulkExportsApi.getUserListExports(organizationId);
    }

    public static OrganizationExportResponse bulkExportUserData(
            BulkExportsApi bulkExportsApi,
            UUID organizationId,
            UUID exportId) throws ApiException
    {
        return bulkExportsApi.getUserListExport(organizationId, exportId);
    }

    public static OrganizationExportResponse createUserListExport(
            BulkExportsApi bulkExportsApi,
            UUID organizationId) throws ApiException
    {
        OrganizationExportRequest request = new OrganizationExportRequest();
        request.setType("organization_memberships_export");
        return bulkExportsApi.createUserListExport(organizationId, request);
    }
}
