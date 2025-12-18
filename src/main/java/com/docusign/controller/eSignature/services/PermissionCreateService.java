package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.DsModelUtils;
import com.docusign.core.model.AccountRoleSettingsPatch;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountRoleSettings;
import com.docusign.esign.model.PermissionProfile;
import com.google.gson.Gson;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class PermissionCreateService {
    public static PermissionProfile createNewProfile(
            AccountsApi accountsApi,
            String accountId,
            String permissionProfileName
    ) throws ApiException {
        // Step 3. Construct your request body
        // We're extending the AccountSettings class with AccountSettingsPatch to include the signingUIVersion which is missing in the swagger spec at this time.
        //ds-snippet-start:eSign24Step3
        Gson gson = new Gson();
        String signingUiVersion = "1";
        AccountRoleSettings settings = DsModelUtils.createDefaultRoleSettings();
        AccountRoleSettingsPatch newSettings = gson.fromJson(gson.toJson(settings), AccountRoleSettingsPatch.class);
        newSettings.signingUiVersion(signingUiVersion);
        PermissionProfile profile = new PermissionProfile()
                .permissionProfileName(permissionProfileName)
                .settings(newSettings);
        //ds-snippet-end:eSign24Step3

        // Step 4. Call the eSignature REST API
        //ds-snippet-start:eSign24Step4
        var permissionProfile = accountsApi.createPermissionProfileWithHttpInfo(accountId, profile, accountsApi.new CreatePermissionProfileOptions());
        Map<String, List<String>> headers = permissionProfile.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return permissionProfile.getData();
        //ds-snippet-end:eSign24Step4
    }
}
