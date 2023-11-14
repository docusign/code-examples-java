package com.docusign.controller.admin.services;

import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.model.OrganizationImportResponse;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BulkImportUserDataService {
    private static final String BULK_IMPORT_TEXT = "AccountID,UserName,UserEmail,PermissionSet\n%s,FirstLast1,User1java@example.com,DS Admin\n%s,FirstLast2,User2java@example.com,DS Sender";

    public static OrganizationImportResponse bulkImportUserData(
            BulkImportsApi bulkImportsApi,
            UUID organizationId,
            UUID accountId
    ) throws Exception {
        // Make sure you're using a verified domain for auto-activation to work properly
        //ds-snippet-start:Admin4Step3
        String csvUserData = String.format(BULK_IMPORT_TEXT, accountId, accountId);
        byte[] csvDataInBytes = csvUserData.getBytes(StandardCharsets.UTF_8);

        return bulkImportsApi.createBulkImportSingleAccountAddUsersRequest(organizationId, accountId, csvDataInBytes);
        //ds-snippet-end:Admin4Step3
    }
}
