package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.GroupsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Group;
import com.docusign.esign.model.GroupInformation;

import java.util.List;

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
        return groupsApi.updateGroups(accountId, groupInformation);
    }
}
