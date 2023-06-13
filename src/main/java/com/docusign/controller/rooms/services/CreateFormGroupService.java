package com.docusign.controller.rooms.services;

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
        //ds-snippet-start:Rooms7Step3
        FormGroupForCreate formGroupForCreate = new FormGroupForCreate()
                .name(formGroupName);
        //ds-snippet-end:Rooms7Step3

        //ds-snippet-start:Rooms7Step4
        return formGroupsApi.createFormGroup(accountId, formGroupForCreate);
        //ds-snippet-end:Rooms7Step4
    }
}
