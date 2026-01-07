package com.docusign.controller.eSignature.services;

import com.docusign.core.utils.DateUtils;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.UsersApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public final class SharedAccessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedAccessService.class);

    private static final int FROM_DATE_OFFSET_DAYS = 10;

    private static final String MANAGE = "manage";

    public UserInformation getUserInfo(UsersApi usersApi, String accountId, String agentEmail) {
        UserInformation userInformation = null;
        UsersApi.CallListOptions callListOptions = usersApi.new CallListOptions();
        callListOptions.setEmail(agentEmail);

        try {
            var informationList = usersApi.callListWithHttpInfo(accountId, callListOptions);
            Map<String, List<String>> headers = informationList.getHeaders();
            java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
            java.util.List<String> reset = headers.get("X-RateLimit-Reset");

            if (remaining != null & reset != null) {
                Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
                System.out.println("API calls remaining: " + remaining);
                System.out.println("Next Reset: " + resetInstant);
            }

            if (Integer.parseInt(informationList.getData().getResultSetSize()) > 0) {
                userInformation = informationList.getData().getUsers().stream()
                        .filter(user -> "Active".equals(user.getUserStatus())).findFirst().get();
            }
        } catch (ApiException e) {
            LOGGER.error(e.getMessage());
        }

        return userInformation;
    }

    public NewUsersSummary createAgent(
            UsersApi usersApi,
            String accountId,
            String agentEmail,
            String agentName,
            String activation) throws ApiException {
        //ds-snippet-start:eSign43Step3
        UserInformation user = new UserInformation();
        user.email(agentEmail);
        user.userName(agentName);
        user.activationAccessCode(activation);

        NewUsersDefinition usersDefinition = new NewUsersDefinition();
        usersDefinition.addNewUsersItem(user);

        var createUser = usersApi.createWithHttpInfo(accountId, usersDefinition);
        Map<String, java.util.List<String>> headers = createUser.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return createUser.getData();
        //ds-snippet-end:eSign43Step3
    }

    public void activateAgent(
            AccountsApi accountsApi,
            String accountId,
            String userId,
            String createdUserId) throws ApiException {
        AccountsApi.GetAgentUserAuthorizationsOptions options = accountsApi.new GetAgentUserAuthorizationsOptions();
        options.setPermissions(MANAGE);
        var userAuthorizations = accountsApi.getAgentUserAuthorizationsWithHttpInfo(accountId, createdUserId, options);
        Map<String, java.util.List<String>> headers = userAuthorizations.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        if (userAuthorizations.getData().getAuthorizations() == null
                || userAuthorizations.getData().getAuthorizations().isEmpty()) {
            //ds-snippet-start:eSign43Step4
            AuthorizationUser user = new AuthorizationUser();
            user.userId(createdUserId);
            user.accountId(accountId);

            UserAuthorizationCreateRequest userAuthorizationCreateRequest = new UserAuthorizationCreateRequest();
            userAuthorizationCreateRequest.agentUser(user);
            userAuthorizationCreateRequest.permission(MANAGE);

            var createAuthorization = accountsApi.createUserAuthorizationWithHttpInfo(accountId, userId,
                    userAuthorizationCreateRequest);
            headers = createAuthorization.getHeaders();
            remaining = headers.get("X-RateLimit-Remaining");
            reset = headers.get("X-RateLimit-Reset");

            if (remaining != null & reset != null) {
                Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
                System.out.println("API calls remaining: " + remaining);
                System.out.println("Next Reset: " + resetInstant);
            }
            //ds-snippet-end:eSign43Step4
        }
    }

    public EnvelopesInformation getEnvelopeInfo(
            EnvelopesApi envelopesApi,
            String accountId) throws ApiException {
        //ds-snippet-start:eSign43Step5
        EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(FROM_DATE_OFFSET_DAYS);
        options.setFromDate(DateUtils.DATE_WITH_SLASH.format(date));

        var listStatusResponse = envelopesApi.listStatusChangesWithHttpInfo(accountId, options);
        Map<String, java.util.List<String>> headers = listStatusResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return listStatusResponse.getData();
        //ds-snippet-end:eSign43Step5
    }
}
