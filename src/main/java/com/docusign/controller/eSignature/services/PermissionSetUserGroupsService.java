package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.GroupsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Group;
import com.docusign.esign.model.GroupInformation;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class PermissionSetUserGroupsService {
    public static GroupInformation permissionSetUserGroups(
            ApiClient apiClient,
            String groupId,
            String profileId,
            String accountId
    ) throws ApiException {
        GroupsApi groupsApi = new GroupsApi(apiClient);

        // Step 3: Perform request
        Group newGroup = new Group()
                .groupId(groupId)
                .permissionProfileId(profileId);
        GroupInformation groupInformation = new GroupInformation()
                .groups(List.of(newGroup));

        var groupResponse = groupsApi.updateGroupsWithHttpInfo(accountId, groupInformation);
        Map<String, List<String>> headers = groupResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return groupResponse.getData();
    }
}
