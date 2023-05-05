package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.UsersApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class SharedAccessService {

    private static final int FROM_DATE_OFFSET_DAYS = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    public static final String MANAGE = "manage";

    public static UserInformation checkIfAgentExists(UsersApi usersApi, String accountId, String agentEmail) {
        UsersApi.CallListOptions callListOptions = usersApi.new CallListOptions();
        callListOptions.setEmail(agentEmail);

        try {
            UserInformationList informationList = usersApi.callList(accountId, callListOptions);

            if (Integer.parseInt(informationList.getResultSetSize()) > 0) {
                UserInformation userInformation = informationList.getUsers().stream().filter(user -> user.getUserStatus().equals("Active")).findFirst().get();

                return userInformation;
            }
        } catch (ApiException e) {
            return null;
        }

        return null;
    }

    public static NewUsersSummary createAgent(
            UsersApi usersApi,
            String accountId,
            String agentEmail,
            String agentName,
            String activation
    ) throws ApiException {
        UserInformation user = new UserInformation();
        user.email(agentEmail);
        user.userName(agentName);
        user.activationAccessCode(activation);

        NewUsersDefinition usersDefinition = new NewUsersDefinition();
        usersDefinition.addNewUsersItem(user);

        return usersApi.create(accountId, usersDefinition);
    }

    public static void activateAgent(
            AccountsApi accountsApi,
            String accountId,
            String userId,
            String createdUserId
    ) throws ApiException {
        AccountsApi.GetAgentUserAuthorizationsOptions options = accountsApi.new GetAgentUserAuthorizationsOptions();
        options.setPermissions(MANAGE);
        UserAuthorizations userAuthorizations = accountsApi.getAgentUserAuthorizations(accountId, createdUserId, options);

        if (userAuthorizations.getAuthorizations() == null || userAuthorizations.getAuthorizations().isEmpty() ) {
            AuthorizationUser user = new AuthorizationUser();
            user.userId(createdUserId);
            user.accountId(accountId);

            UserAuthorizationCreateRequest userAuthorizationCreateRequest = new UserAuthorizationCreateRequest();
            userAuthorizationCreateRequest.agentUser(user);
            userAuthorizationCreateRequest.permission(MANAGE);

            accountsApi.createUserAuthorization(accountId, userId, userAuthorizationCreateRequest);
        }
    }

    public static EnvelopesInformation getEnvelopes(
            EnvelopesApi envelopesApi,
            String accountId
    ) throws ApiException {
        EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(FROM_DATE_OFFSET_DAYS);
        options.setFromDate(DATE_FORMATTER.format(date));

        return envelopesApi.listStatusChanges(accountId, options);
    }
}
