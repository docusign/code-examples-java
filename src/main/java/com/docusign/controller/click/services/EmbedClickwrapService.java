package com.docusign.controller.click.services;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.client.ApiResponse;
import com.docusign.click.model.ClickwrapVersionResponse;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.click.model.UserAgreementRequest;
import com.docusign.click.model.UserAgreementResponse;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EmbedClickwrapService {
    public static String createAgreementUrl(
            AccountsApi accountsApi,
            String accountId,
            String clickwrapId,
            String fullName,
            String email,
            String company,
            String title,
            String date) throws ApiException {

        //ds-snippet-start:Click6Step3
        UserAgreementRequest userAgreementRequest = new UserAgreementRequest();
        userAgreementRequest.setClientUserId(email);
        Map<String, String> documentData = new HashMap<String, String>();
        documentData.put("fullName", fullName);
        documentData.put("email", email);
        documentData.put("company", company);
        documentData.put("title", title);
        documentData.put("date", date);
        userAgreementRequest.setDocumentData(documentData);
        //ds-snippet-end:Click6Step3
        //ds-snippet-start:Click6Step4
        ApiResponse<UserAgreementResponse> response = accountsApi.createHasAgreedWithHttpInfo(accountId, clickwrapId,
                userAgreementRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        if (response.getStatusCode() == 201) {
            return response.getData().getAgreementUrl();
        }
        return "Already Agreed";
        //ds-snippet-end:Click6Step4
    }

    public static ClickwrapVersionsResponse getActiveClickwraps(
            AccountsApi accountsApi,
            String accountId) throws ApiException {
        AccountsApi.GetClickwrapsOptions options = accountsApi.new GetClickwrapsOptions();
        options.setStatus("active");
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
