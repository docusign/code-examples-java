package com.docusign.controller.admin.services;

import com.docusign.admin.api.AccountsApi;
import com.docusign.admin.api.OrganizationsApi;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.IndividualMembershipDataRedactionRequest;
import com.docusign.admin.model.IndividualUserDataRedactionResponse;
import com.docusign.admin.model.UserDrilldownResponse;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.admin.model.MembershipDataRedactionRequest;
import com.docusign.admin.model.IndividualUserDataRedactionRequest;

import java.util.Arrays;
import java.util.UUID;

public class UserDataManagementService {
    public IndividualUserDataRedactionResponse deleteUserDataFromAccountByUserId(
            AccountsApi accountsApi,
            UUID userId,
            UUID accountId
    ) throws Exception {
        //ds-snippet-start:Admin11Step3
        IndividualMembershipDataRedactionRequest membershipDataRedaction = new IndividualMembershipDataRedactionRequest();
        membershipDataRedaction.userId(userId);
        //ds-snippet-end:Admin11Step3
        //ds-snippet-start:Admin11Step4
        return accountsApi.redactIndividualMembershipData(accountId, membershipDataRedaction);
        //ds-snippet-end:Admin11Step4
    }

    public IndividualUserDataRedactionResponse deleteUserDataFromOrganizationByEmail(
            UsersApi usersApi,
            OrganizationsApi organizationsApi,
            String emailAddress,
            UUID organizationId
    ) throws ApiException {
        UsersApi.GetUserDSProfilesByEmailOptions getProfilesOptions = usersApi.new GetUserDSProfilesByEmailOptions();
        getProfilesOptions.setEmail(emailAddress);

        UsersDrilldownResponse profiles = usersApi.getUserDSProfilesByEmail(organizationId, getProfilesOptions);

        UserDrilldownResponse user = null;
        if (profiles.getUsers() != null && !profiles.getUsers().isEmpty() && profiles.getUsers().get(0) != null) {
            user = profiles.getUsers().get(0);
        }

        MembershipDataRedactionRequest memberships = new MembershipDataRedactionRequest();

        if (user.getMemberships() != null && !user.getMemberships().isEmpty() && user.getMemberships().get(0) != null) {
            memberships.accountId(user.getMemberships().get(0).getAccountId());
        }

		//ds-snippet-start:Admin10Step3
		IndividualUserDataRedactionRequest userRedactionRequest = new IndividualUserDataRedactionRequest();
		userRedactionRequest.setUserId(user.getId());
		userRedactionRequest.setMemberships(Arrays.asList(memberships));
		//ds-snippet-end:Admin10Step3
		//ds-snippet-start:Admin10Step4
		return organizationsApi.redactIndividualUserData(organizationId, userRedactionRequest);
		//ds-snippet-end:Admin10Step4
	}
}