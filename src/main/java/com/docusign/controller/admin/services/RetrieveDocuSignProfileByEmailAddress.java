package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.UsersDrilldownResponse;

import java.util.UUID;

public class RetrieveDocuSignProfileByEmailAddress {
    public static UsersDrilldownResponse getDocuSignProfileByEmailAddress(
            UsersApi usersApi,
            UUID organizationId,
            String emailAddress
    ) throws Exception {
        var userOptions = usersApi.new GetUserDSProfilesByEmailOptions();
        userOptions.setEmail(emailAddress);
        return usersApi.getUserDSProfilesByEmail(organizationId, userOptions);
    }
}
