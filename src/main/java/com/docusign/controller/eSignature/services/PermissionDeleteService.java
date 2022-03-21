package com.docusign.controller.eSignature.services;


import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;

public final class PermissionDeleteService {
    public static void permissionDelete(
            AccountsApi accountsApi,
            String accountId,
            String curProfileId
    ) throws ApiException {
        accountsApi.deletePermissionProfile(accountId, curProfileId);
    }
}
