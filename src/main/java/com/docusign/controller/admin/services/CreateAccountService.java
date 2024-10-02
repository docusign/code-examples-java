package com.docusign.controller.admin.services;

import com.docusign.admin.api.ProvisionAssetGroupApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.OrganizationSubscriptionResponse;
import com.docusign.admin.model.SubAccountCreateRequest;
import com.docusign.admin.model.SubAccountCreateRequestSubAccountCreationSubscription;
import com.docusign.admin.model.SubAccountCreateRequestSubAccountCreationTargetAccountAdmin;
import com.docusign.admin.model.SubAccountCreateRequestSubAccountCreationTargetAccountDetails;
import com.docusign.admin.model.SubscriptionProvisionModelAssetGroupWorkResult;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class CreateAccountService {

    private static final String DEFAULT_ACCOUNT_NAME = "CreatedThroughAPI";

    private static final String DEFAULT_COUNTRY_CODE = "US";

    //ds-snippet-start:Admin13Step3
    public static OrganizationSubscriptionResponse getFirstPlanItem(
            ProvisionAssetGroupApi provisionAssetGroupApi,
            UUID orgId) throws ApiException {
        return provisionAssetGroupApi.getOrganizationPlanItems(orgId).stream().findFirst().orElse(null);
    }
    //ds-snippet-end:Admin13Step3

    //ds-snippet-start:Admin13Step5
    public static SubscriptionProvisionModelAssetGroupWorkResult createAccountBySubscription(
            ProvisionAssetGroupApi provisionAssetGroupApi,
            UUID orgId,
            String email,
            String firstName,
            String lastName,
            String subscriptionId,
            UUID planId) throws ApiException {
        SubAccountCreateRequest subAccountRequest = buildSubAccountRequest(email, firstName, lastName, subscriptionId,
                planId);

        return provisionAssetGroupApi.createAssetGroupAccount(orgId, subAccountRequest);
    }
    //ds-snippet-end:Admin13Step5

    //ds-snippet-start:Admin13Step4
    private static SubAccountCreateRequest buildSubAccountRequest(
            String email,
            String firstName,
            String lastName,
            String subscriptionId,
            UUID planId) {
        List<UUID> uuidList = new ArrayList<>();
        SubAccountCreateRequest subAccountRequest = new SubAccountCreateRequest();
        SubAccountCreateRequestSubAccountCreationSubscription subscriptionDetails = new SubAccountCreateRequestSubAccountCreationSubscription();
        subscriptionDetails.setId(subscriptionId);
        subscriptionDetails.setPlanId(planId);
        subscriptionDetails.setModules(uuidList);

        SubAccountCreateRequestSubAccountCreationTargetAccountDetails targetAccount = new SubAccountCreateRequestSubAccountCreationTargetAccountDetails();
        targetAccount.setName(DEFAULT_ACCOUNT_NAME);
        targetAccount.setCountryCode(DEFAULT_COUNTRY_CODE);

        SubAccountCreateRequestSubAccountCreationTargetAccountAdmin admin = new SubAccountCreateRequestSubAccountCreationTargetAccountAdmin();
        admin.setEmail(email);
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setLocale(SubAccountCreateRequestSubAccountCreationTargetAccountAdmin.LocaleEnum.EN);

        targetAccount.setAdmin(admin);
        subAccountRequest.setSubscriptionDetails(subscriptionDetails);
        subAccountRequest.setTargetAccount(targetAccount);

        return subAccountRequest;
    }
    //ds-snippet-end:Admin13Step4
}
