package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.RolesApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.RoleSummary;
import com.docusign.rooms.model.RoleSummaryList;

public final class GetAdminRolesService {
    public static RoleSummary getAdminRole(RolesApi rolesApi, String accountId)
            throws ApiException {
        RoleSummaryList roleSummaryList = rolesApi.getRoles(accountId);
        return roleSummaryList.getRoles()
                .stream()
                .filter(RoleSummary::isIsDefaultForAdmin)
                .findFirst()
                .get();
    }
}
