package com.docusign.controller.click.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.client.ApiResponse;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionResponse;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.controller.click.examples.ClickwrapHelper;

public final class ActivateClickwrapService {
    public static ClickwrapVersionSummaryResponse activateClickwrap(
            AccountsApi accountsApi,
            String accountId,
            String clickwrapId,
            String clickwrapVersionNumber) throws ApiException {
        //ds-snippet-start:Click2Step3
        ClickwrapRequest clickwrapRequest = new ClickwrapRequest().status(ClickwrapHelper.STATUS_ACTIVE);
        //ds-snippet-end:Click2Step3

        //ds-snippet-start:Click2Step4
        ApiResponse<ClickwrapVersionSummaryResponse> response = accountsApi.updateClickwrapVersionWithHttpInfo(
                accountId,
                clickwrapId,
                clickwrapVersionNumber,
                clickwrapRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        //ds-snippet-end:Click2Step4
    }

    public static ClickwrapVersionsResponse getClickwrapsByStatus(
            AccountsApi accountsApi,
            String accountId,
            String status) throws ApiException {
        AccountsApi.GetClickwrapsOptions options = accountsApi.new GetClickwrapsOptions();
        options.setStatus(status);

        ApiResponse<ClickwrapVersionsResponse> response = accountsApi.getClickwrapsWithHttpInfo(accountId, options);

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
