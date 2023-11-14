package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.DsModelUtils;
import com.docusign.core.model.AccountRoleSettingsPatch;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountRoleSettings;
import com.docusign.esign.model.PermissionProfile;
import com.docusign.esign.model.PermissionProfileInformation;
import com.google.gson.Gson;

import java.util.*;

public final class PermissionChangeSingleSettingService {
    public static PermissionProfile permissionChangeSingleSetting(
            AccountsApi accountsApi,
            String accountId,
            String curProfileId
    ) throws ApiException {
        // Step 3. Construct your request body
        //ds-snippet-start:eSign26Step3
        PermissionProfileInformation permissionsInfo = accountsApi.listPermissions(accountId);
        PermissionProfile profile = PermissionChangeSingleSettingService.findProfile(
                        permissionsInfo.getPermissionProfiles(), curProfileId)
                .orElseThrow(NoSuchElementException::new);
        //ds-snippet-end:eSign26Step3

        // Step 4. Call the eSignature REST API
        //ds-snippet-start:eSign26Step4
        AccountRoleSettings newSettings = Objects.requireNonNullElse(profile.getSettings(),
                DsModelUtils.createDefaultRoleSettings());
        profile.setSettings(PermissionChangeSingleSettingService.changeRandomSettings(newSettings));
        return accountsApi.updatePermissionProfile(accountId, curProfileId, profile);
        //ds-snippet-end:eSign26Step4
    }

    public static Optional<PermissionProfile> findProfile(List<PermissionProfile> profiles, String profileId) {
        return profiles.stream().filter(p -> p.getPermissionProfileId().equals(profileId)).findFirst();
    }

    // Changes random boolean properties; in a real application, changing properties
    // will be read from the page or in a different way
    private static AccountRoleSettings changeRandomSettings(AccountRoleSettings settings) {
        Gson gson = new Gson();
        String signingUiVersion = "1";
        // Change this value back to: gson.fromJson(gson.toJson(settings),
        // AccountRoleSettings.class);
        // as soon as the signinguiversion is added back to the swagger spec.
        // Also change the type back from AccountRoleSettingsPatch to AccountRoleSettings
        AccountRoleSettingsPatch newSettings = gson.fromJson(gson.toJson(settings), AccountRoleSettingsPatch.class);
        newSettings.signingUiVersion(signingUiVersion);
        Random random = new Random(System.currentTimeMillis());

        return newSettings.canCreateWorkspaces(randomBool(random)).allowEnvelopeSending(randomBool(random))
                .allowSignerAttachments(randomBool(random)).allowESealRecipients(randomBool(random))
                .allowTaggingInSendAndCorrect(randomBool(random)).allowWetSigningOverride(randomBool(random))
                .enableApiRequestLogging(randomBool(random)).enableRecipientViewingNotifications(randomBool(random))
                .allowSupplementalDocuments(randomBool(random)).disableDocumentUpload(randomBool(random));
    }

    private static String randomBool(Random random) {
        if (random.nextBoolean()) {
            return DsModelUtils.TRUE;
        }
        return DsModelUtils.FALSE;
    }
}
