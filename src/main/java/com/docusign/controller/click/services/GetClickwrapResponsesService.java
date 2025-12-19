package com.docusign.controller.click.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.api.AccountsApi.GetClickwrapAgreementsOptions;
import com.docusign.click.client.ApiException;
import com.docusign.click.client.ApiResponse;
import com.docusign.click.model.ClickwrapAgreementsResponse;

public final class GetClickwrapResponsesService {
    public static ClickwrapAgreementsResponse getClickwrapResponses(
            AccountsApi accountsApi,
            String accountId,
            String clickwrapId) throws ApiException {
        // Step 3: Call the v1 Click API
        ApiResponse<ClickwrapAgreementsResponse> response = accountsApi.getClickwrapAgreementsWithHttpInfo(accountId,
                clickwrapId, (GetClickwrapAgreementsOptions) null);

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
