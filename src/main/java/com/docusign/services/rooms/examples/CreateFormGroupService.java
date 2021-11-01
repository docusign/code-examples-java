package com.docusign.services.rooms.examples;

import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormGroup;
import com.docusign.rooms.model.FormGroupForCreate;

public final class CreateFormGroupService {
    public static FormGroup createFormGroup(
            FormGroupsApi formGroupsApi,
            String accountId,
            String formGroupName
    ) throws ApiException {
        // Step 3 Start
        FormGroupForCreate formGroupForCreate = new FormGroupForCreate()
                .name(formGroupName);
        // Step 3 End

        // Step 4 Start
        return formGroupsApi.createFormGroup(accountId, formGroupForCreate);
        // Step 4 End
    }
}
