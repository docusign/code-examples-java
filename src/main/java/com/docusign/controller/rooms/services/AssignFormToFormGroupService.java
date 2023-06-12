package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormGroupFormToAssign;

import java.util.UUID;

public final class AssignFormToFormGroupService {
    public static FormGroupFormToAssign assignFormToFormGroup(
            FormGroupsApi formGroupsApi,
            String accountId,
            UUID formId,
            UUID formGroupId
    ) throws ApiException {
        //ds-snippet-start:Rooms9Step5
        FormGroupFormToAssign formGroupFormToAssignRequest = new FormGroupFormToAssign()
                .formId(formId);
        //ds-snippet-end:Rooms9Step5

        return formGroupsApi.assignFormGroupForm(
                accountId, formGroupId, formGroupFormToAssignRequest);

    }
}
