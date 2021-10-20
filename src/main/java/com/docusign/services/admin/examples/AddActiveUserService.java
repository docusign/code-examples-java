package com.docusign.services.admin.examples;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.*;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.UUID;

public  class AddActiveUserService {
    public static NewUserResponse addActiveUser(
            WorkArguments args,
            UsersApi usersApi,
            UUID organizationId,
            UUID accountId) throws Exception
    {
        return createNewActiveUser(args, usersApi, organizationId, accountId);
    }

    protected static NewUserResponse createNewActiveUser(
            WorkArguments args,
            UsersApi usersApi,
            UUID organizationId,
            UUID accountId) throws Exception
    {
        // Step 5 start
        java.util.List<GroupRequest> groups = new ArrayList<>();
        groups.add(new GroupRequest().id(Long.valueOf(args.getGroupId())));

        // Fill the request with data from the form
        NewUserRequest accountUserRequest = new NewUserRequest()
                .defaultAccountId(accountId)
                .addAccountsItem(
                        new NewUserRequestAccountProperties()
                                .id(accountId)
                                .permissionProfile(
                                        new PermissionProfileRequest()
                                                .id(Long.valueOf(args.getProfileId()))
                                )
                                .groups(groups)
                )
                .email(args.getEmail())
                .userName(args.getUserName())
                .firstName(args.getFirstName())
                .lastName(args.getLastName())
                .autoActivateMemberships(true);
        // Step 5 end

        // Step 6 start
        return usersApi.createUser(organizationId, accountUserRequest);
        // Step 6 end
    }
}
