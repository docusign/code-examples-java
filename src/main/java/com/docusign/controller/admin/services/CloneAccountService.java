package com.docusign.controller.admin.services;

import com.docusign.admin.api.ProvisionAssetGroupApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.AssetGroupAccountClone;
import com.docusign.admin.model.AssetGroupAccountsResponse;
import com.docusign.admin.model.AssetGroupAccountCloneSourceAccount;
import com.docusign.admin.model.AssetGroupAccountCloneTargetAccount;
import com.docusign.admin.model.AssetGroupAccountCloneTargetAccountAdmin;

import java.util.UUID;

public class CloneAccountService {
    public AssetGroupAccountsResponse getGroupAccounts(
        ProvisionAssetGroupApi provisionAssetGroupApi,
        UUID organizationId
    ) throws ApiException {

        //ds-snippet-start:Admin12Step3
        ProvisionAssetGroupApi.GetAssetGroupAccountsOptions options = provisionAssetGroupApi.new GetAssetGroupAccountsOptions();
        options.setCompliant(true);
        return provisionAssetGroupApi.getAssetGroupAccounts(organizationId, options);
        //ds-snippet-end:Admin12Step3
    }

    public AssetGroupAccountClone getClonedAccount(
        ProvisionAssetGroupApi provisionAssetGroupApi,
        UUID organizationId,
        UUID sourceAccountId,
        String targetAccountName,
        String targetAccountEmail,
        String targetAccountFirstName,
        String targetAccountLastName
    ) throws ApiException {
        //ds-snippet-start:Admin12Step4
        String countryCode = "US";

        AssetGroupAccountClone accountData = new AssetGroupAccountClone();
        AssetGroupAccountCloneSourceAccount sourceAccount = new AssetGroupAccountCloneSourceAccount();
        sourceAccount.setId(sourceAccountId);

        AssetGroupAccountCloneTargetAccount targetAccount = new AssetGroupAccountCloneTargetAccount();
        targetAccount.setName(targetAccountName);
        targetAccount.setCountryCode(countryCode);

        AssetGroupAccountCloneTargetAccountAdmin admin = new AssetGroupAccountCloneTargetAccountAdmin();
        admin.setEmail(targetAccountEmail);
        admin.setFirstName(targetAccountFirstName);
        admin.setLastName(targetAccountLastName);

        targetAccount.admin(admin);
        accountData.setSourceAccount(sourceAccount);
        accountData.setTargetAccount(targetAccount);
        //ds-snippet-end:Admin12Step4

        //ds-snippet-start:Admin12Step5
        return provisionAssetGroupApi.cloneAssetGroupAccount(organizationId, accountData);
        //ds-snippet-end:Admin12Step5
    }
}
