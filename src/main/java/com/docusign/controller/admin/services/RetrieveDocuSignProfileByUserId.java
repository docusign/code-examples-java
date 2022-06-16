package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.UsersDrilldownResponse;

import java.util.UUID;

public class RetrieveDocuSignProfileByUserId {
    public static UsersDrilldownResponse getDocuSignProfileByUserId(
            UsersApi usersApi,
            UUID organizationId,
            UUID userId
    ) throws Exception {
        return usersApi.getUserDSProfile(organizationId, userId);
    }
}
