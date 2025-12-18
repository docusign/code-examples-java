package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddActiveUserService {
    //ds-snippet-start:Admin1Step5
    public static NewUserResponse createNewActiveUser(
            String groupId,
            String profileId,
            String email,
            String userName,
            String firstName,
            String lastName,
            UsersApi usersApi,
            UUID organizationId,
            UUID accountId) throws Exception
    {
        java.util.List<GroupRequest> groups = new ArrayList<>();
        groups.add(new GroupRequest().id(Long.valueOf(groupId)));

        // Fill the request with data from the form
        NewUserRequest accountUserRequest = new NewUserRequest()
                .defaultAccountId(accountId)
                .addAccountsItem(
                        new NewUserRequestAccountProperties()
                                .id(accountId)
                                .permissionProfile(
                                        new PermissionProfileRequest()
                                                .id(Long.valueOf(profileId))
                                )
                                .groups(groups)
                )
                .email(email)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .autoActivateMemberships(true);
        //ds-snippet-end:Admin1Step5

        //ds-snippet-start:Admin1Step6
        ApiResponse<NewUserResponse> response = usersApi.createUserWithHttpInfo(organizationId, accountUserRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        //ds-snippet-end:Admin1Step6
    }
}
