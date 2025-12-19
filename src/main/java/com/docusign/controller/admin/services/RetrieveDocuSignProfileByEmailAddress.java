package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.UsersDrilldownResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RetrieveDocuSignProfileByEmailAddress {
    public static UsersDrilldownResponse getDocuSignProfileByEmailAddress(
            UsersApi usersApi,
            UUID organizationId,
            String emailAddress) throws Exception {
        var userOptions = usersApi.new GetUserDSProfilesByEmailOptions();
        userOptions.setEmail(emailAddress);
        ApiResponse<UsersDrilldownResponse> response = usersApi.getUserDSProfilesByEmailWithHttpInfo(organizationId,
                userOptions);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
