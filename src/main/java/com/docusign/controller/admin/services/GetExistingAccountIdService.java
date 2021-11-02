package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.UsersDrilldownResponse;

import java.util.UUID;

public final class GetExistingAccountIdService {
    /**
	* Creates a request to get current user.
	* @param usersApi instance of users api
	* @param signerEmail signer email
	* @param organizationId the ID of current organization
	* @return the account Id
	*/
    public static UUID getExistingAccountId(
		  UsersApi usersApi,
		  String signerEmail,
		  UUID organizationId
    ) throws Exception {
	   // set the signer email to get an information about that user
	   // new GetUsersOptions() will throw "an enclosing instance that contains com.docusign.admin.api.UsersApi.GetUserProfilesOptions is required"
	   // at compile time.  I used this workaround from stackOverflow:
	   // https://stackoverflow.com/a/4297913/2226328

	   UsersApi.GetUserProfilesOptions userProfilesOptions = usersApi.new GetUserProfilesOptions();
	   userProfilesOptions.setEmail(signerEmail);

	   UsersDrilldownResponse user = usersApi.getUserProfiles(organizationId, userProfilesOptions);

	   if (user.getUsers().isEmpty()) {
		  throw new Exception("Could not get an account id from the request.");
	   }

	   return  user.getUsers().get(0).getDefaultAccountId();
    }

}
