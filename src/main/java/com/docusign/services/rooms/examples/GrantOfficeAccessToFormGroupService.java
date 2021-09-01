package com.docusign.services.rooms.examples;

import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;

import java.util.UUID;

public final class GrantOfficeAccessToFormGroupService {
    public static void GrantOfficeAccessToFormGroup(
            FormGroupsApi formGroupsApi,
            String accountId,
            UUID formGroupId,
            Integer officeId) throws ApiException {
        formGroupsApi.grantOfficeAccessToFormGroup(
                accountId,
                formGroupId,
                officeId);
    }
}
