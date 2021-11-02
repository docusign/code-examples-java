package com.services.eSignature;

import com.docusign.controller.eSignature.examples.DsModelUtils;
import com.docusign.core.model.AccountRoleSettingsPatch;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountRoleSettings;
import com.docusign.esign.model.PermissionProfile;
import com.google.gson.Gson;

public class PermissionCreateService {
    public static PermissionProfile createNewProfile(
            AccountsApi accountsApi,
            String accountId,
            String permissionProfileName
    ) throws ApiException {
        // Step 3. Construct your request body
        // We're extending the AccountSettings class with AccountSettingsPatch to include the signingUIVersion which is missing in the swagger spec at this time.
        Gson gson = new Gson();
        AccountRoleSettings settings = DsModelUtils.createDefaultRoleSettings();
        AccountRoleSettingsPatch newSettings = gson.fromJson(gson.toJson(settings), AccountRoleSettingsPatch.class);
        newSettings.signingUiVersion("1");
        PermissionProfile profile = new PermissionProfile()
                .permissionProfileName(permissionProfileName)
                .settings(newSettings);

        // Step 4. Call the eSignature REST API
        return accountsApi.createPermissionProfile(accountId, profile);
    }
}
