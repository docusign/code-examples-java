package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
import com.docusign.rooms.model.FormGroupFormToAssign;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class AssignFormToFormGroupService {
    public static FormGroupFormToAssign assignFormToFormGroup(
            FormGroupsApi formGroupsApi,
            String accountId,
            UUID formId,
            UUID formGroupId) throws ApiException {
        // ds-snippet-start:Rooms9Step5
        FormGroupFormToAssign formGroupFormToAssignRequest = new FormGroupFormToAssign()
                .formId(formId);
        // ds-snippet-end:Rooms9Step5

        ApiResponse<FormGroupFormToAssign> response = formGroupsApi.assignFormGroupFormWithHttpInfo(
                accountId, formGroupId, formGroupFormToAssignRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
