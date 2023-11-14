package com.docusign.controller.eSignature.services;

import com.docusign.core.utils.DateUtils;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.UsersApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public final class SharedAccessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SharedAccessService.class);

    private static final int FROM_DATE_OFFSET_DAYS = 10;

    private static final String MANAGE = "manage";

    public UserInformation getUserInfo(UsersApi usersApi, String accountId, String agentEmail) {
        UserInformation userInformation = null;
        UsersApi.CallListOptions callListOptions = usersApi.new CallListOptions();
        callListOptions.setEmail(agentEmail);

        try {
            UserInformationList informationList = usersApi.callList(accountId, callListOptions);

            if (Integer.parseInt(informationList.getResultSetSize()) > 0) {
                userInformation = informationList.getUsers().stream()
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
            String activation
    ) throws ApiException {
        //ds-snippet-start:eSign43Step3
        UserInformation user = new UserInformation();
        user.email(agentEmail);
        user.userName(agentName);
        user.activationAccessCode(activation);

        NewUsersDefinition usersDefinition = new NewUsersDefinition();
        usersDefinition.addNewUsersItem(user);

        return usersApi.create(accountId, usersDefinition);
        //ds-snippet-end:eSign43Step3
    }

    public void activateAgent(
            AccountsApi accountsApi,
            String accountId,
            String userId,
            String createdUserId
    ) throws ApiException {
        AccountsApi.GetAgentUserAuthorizationsOptions options = accountsApi.new GetAgentUserAuthorizationsOptions();
        options.setPermissions(MANAGE);
        UserAuthorizations userAuthorizations = accountsApi.getAgentUserAuthorizations(accountId, createdUserId, options);

        if (userAuthorizations.getAuthorizations() == null || userAuthorizations.getAuthorizations().isEmpty()) {
            //ds-snippet-start:eSign43Step4
            AuthorizationUser user = new AuthorizationUser();
            user.userId(createdUserId);
            user.accountId(accountId);

            UserAuthorizationCreateRequest userAuthorizationCreateRequest = new UserAuthorizationCreateRequest();
            userAuthorizationCreateRequest.agentUser(user);
            userAuthorizationCreateRequest.permission(MANAGE);

            accountsApi.createUserAuthorization(accountId, userId, userAuthorizationCreateRequest);
            //ds-snippet-end:eSign43Step4
        }
    }

    public EnvelopesInformation getEnvelopeInfo(
            EnvelopesApi envelopesApi,
            String accountId
    ) throws ApiException {
        //ds-snippet-start:eSign43Step5
        EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(FROM_DATE_OFFSET_DAYS);
        options.setFromDate(DateUtils.DATE_WITH_SLASH.format(date));

        return envelopesApi.listStatusChanges(accountId, options);
        //ds-snippet-end:eSign43Step5
    }
}
