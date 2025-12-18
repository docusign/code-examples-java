package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class GrantOfficeAccessToFormGroupService {
    public static void grantOfficeAccessToFormGroup(
            FormGroupsApi formGroupsApi,
            String accountId,
            UUID formGroupId,
            Integer officeId
    ) throws ApiException {
        ApiResponse<Object> response = formGroupsApi.grantOfficeAccessToFormGroupWithHttpInfo(
                accountId,
                formGroupId,
                officeId);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
    }
}
