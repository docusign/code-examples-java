package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;

import java.util.UUID;

public final class GrantOfficeAccessToFormGroupService {
    public static void grantOfficeAccessToFormGroup(
            FormGroupsApi formGroupsApi,
            String accountId,
            UUID formGroupId,
            Integer officeId
    ) throws ApiException {
        formGroupsApi.grantOfficeAccessToFormGroup(
                accountId,
                formGroupId,
                officeId);
    }
}
