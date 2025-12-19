package com.docusign.controller.rooms.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
import com.docusign.rooms.model.FormGroup;
import com.docusign.rooms.model.FormGroupForCreate;

public final class CreateFormGroupService {
    public static FormGroup createFormGroup(
            FormGroupsApi formGroupsApi,
            String accountId,
            String formGroupName) throws ApiException {
        // ds-snippet-start:Rooms7Step3
        FormGroupForCreate formGroupForCreate = new FormGroupForCreate()
                .name(formGroupName);
        // ds-snippet-end:Rooms7Step3

        // ds-snippet-start:Rooms7Step4
        ApiResponse<FormGroup> response = formGroupsApi.createFormGroupWithHttpInfo(accountId, formGroupForCreate);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        // ds-snippet-end:Rooms7Step4
    }
}
