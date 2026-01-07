package com.docusign.controller.rooms.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.rooms.api.RolesApi;
import com.docusign.rooms.api.RolesApi.GetRolesOptions;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
import com.docusign.rooms.model.RoleSummary;
import com.docusign.rooms.model.RoleSummaryList;

public final class GetAdminRolesService {
    public static RoleSummary getAdminRole(RolesApi rolesApi, String accountId)
            throws ApiException {
        ApiResponse<RoleSummaryList> roleSummaryList = rolesApi.getRolesWithHttpInfo(accountId, (GetRolesOptions) null);

        Map<String, List<String>> headers = roleSummaryList.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return roleSummaryList.getData()
                .getRoles()
                .stream()
                .filter(RoleSummary::isIsDefaultForAdmin)
                .findFirst()
                .get();
    }
}
