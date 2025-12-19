package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class PermissionDeleteService {
    public static void permissionDelete(
            AccountsApi accountsApi,
            String accountId,
            String curProfileId) throws ApiException {
        var deletePermissionProfile = accountsApi.deletePermissionProfileWithHttpInfo(accountId, curProfileId,
                accountsApi.new DeletePermissionProfileOptions());

        Map<String, List<String>> headers = deletePermissionProfile.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
    }
}
