package com.docusign.controller.admin.services;

import com.docusign.admin.api.AccountsApi;
import com.docusign.admin.api.OrganizationsApi;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.IndividualMembershipDataRedactionRequest;
import com.docusign.admin.model.IndividualUserDataRedactionResponse;
import com.docusign.admin.model.UserDrilldownResponse;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.admin.model.MembershipDataRedactionRequest;
import com.docusign.admin.model.IndividualUserDataRedactionRequest;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserDataManagementService {
    public IndividualUserDataRedactionResponse deleteUserDataFromAccountByUserId(
            AccountsApi accountsApi,
            UUID userId,
            UUID accountId) throws Exception {
        // ds-snippet-start:Admin11Step3
        IndividualMembershipDataRedactionRequest membershipDataRedaction = new IndividualMembershipDataRedactionRequest();
        membershipDataRedaction.userId(userId);
        // ds-snippet-end:Admin11Step3
        // ds-snippet-start:Admin11Step4
        ApiResponse<IndividualUserDataRedactionResponse> response = accountsApi
                .redactIndividualMembershipDataWithHttpInfo(accountId, membershipDataRedaction);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        // ds-snippet-end:Admin11Step4
    }

    public IndividualUserDataRedactionResponse deleteUserDataFromOrganizationByEmail(
            UsersApi usersApi,
            OrganizationsApi organizationsApi,
            String emailAddress,
            UUID organizationId) throws ApiException {
        UsersApi.GetUserDSProfilesByEmailOptions getProfilesOptions = usersApi.new GetUserDSProfilesByEmailOptions();
        getProfilesOptions.setEmail(emailAddress);

        ApiResponse<UsersDrilldownResponse> profiles = usersApi.getUserDSProfilesByEmailWithHttpInfo(organizationId,
                getProfilesOptions);

        Map<String, List<String>> headers = profiles.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        UserDrilldownResponse user = null;
        if (profiles.getData().getUsers() != null && !profiles.getData().getUsers().isEmpty()
                && profiles.getData().getUsers().get(0) != null) {
            user = profiles.getData().getUsers().get(0);
        }

        MembershipDataRedactionRequest memberships = new MembershipDataRedactionRequest();

        if (user.getMemberships() != null && !user.getMemberships().isEmpty() && user.getMemberships().get(0) != null) {
            memberships.accountId(user.getMemberships().get(0).getAccountId());
        }

        // ds-snippet-start:Admin10Step3
        IndividualUserDataRedactionRequest userRedactionRequest = new IndividualUserDataRedactionRequest();
        userRedactionRequest.setUserId(user.getId());
        userRedactionRequest.setMemberships(Arrays.asList(memberships));
        // ds-snippet-end:Admin10Step3
        // ds-snippet-start:Admin10Step4
        ApiResponse<IndividualUserDataRedactionResponse> response = organizationsApi
                .redactIndividualUserDataWithHttpInfo(organizationId, userRedactionRequest);

        headers = response.getHeaders();
        remaining = headers.get("X-RateLimit-Remaining");
        reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        // ds-snippet-end:Admin10Step4
    }
}