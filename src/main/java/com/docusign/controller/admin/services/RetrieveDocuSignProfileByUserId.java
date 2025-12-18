package com.docusign.controller.admin.services;

import com.docusign.admin.api.UsersApi;
import com.docusign.admin.api.UsersApi.GetUserDSProfileOptions;
import com.docusign.admin.client.ApiResponse;
import com.docusign.admin.model.UsersDrilldownResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RetrieveDocuSignProfileByUserId {
    public static UsersDrilldownResponse getDocuSignProfileByUserId(
            UsersApi usersApi,
            UUID organizationId,
            UUID userId
    ) throws Exception {
        ApiResponse<UsersDrilldownResponse> response = usersApi.getUserDSProfileWithHttpInfo(organizationId, userId, (GetUserDSProfileOptions)null);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
