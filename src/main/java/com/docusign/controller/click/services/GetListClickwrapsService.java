package com.docusign.controller.click.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.api.AccountsApi.GetClickwrapsOptions;
import com.docusign.click.client.ApiException;
import com.docusign.click.client.ApiResponse;
import com.docusign.click.model.ClickwrapVersionsResponse;

public final class GetListClickwrapsService {
    public static ClickwrapVersionsResponse getListClickwrap(
            AccountsApi accountsApi,
            String accountId) throws ApiException {
        // Step 3: Call the v1 Click API
        //ds-snippet-start:Click4Step3
        ApiResponse<ClickwrapVersionsResponse> response = accountsApi.getClickwrapsWithHttpInfo(accountId,
                (GetClickwrapsOptions) null);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        //ds-snippet-end:Click4Step3
    }
}
