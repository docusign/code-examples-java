package com.docusign.services.rooms.examples;

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
        // Step 5 Start
        FormGroupFormToAssign formGroupFormToAssignRequest = new FormGroupFormToAssign()
                .formId(formId);
        // Step 5 End
        // Step 6 Start
        return formGroupsApi.assignFormGroupForm(
                accountId, formGroupId, formGroupFormToAssignRequest);
        // Step 6 End
    }
}
