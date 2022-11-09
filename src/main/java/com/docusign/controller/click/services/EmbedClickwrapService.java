package com.docusign.controller.click.services;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.client.ApiResponse;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.click.model.UserAgreementRequest;
import com.docusign.click.model.UserAgreementResponse;

import java.util.HashMap;
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
            String date
            ) throws ApiException {

                UserAgreementRequest userAgreementRequest = new UserAgreementRequest();
                userAgreementRequest.setClientUserId(email);
                Map<String, String> documentData = new HashMap<String, String>();
                documentData.put("fullName", fullName);
                documentData.put("email", email);
                documentData.put("company", company);
                documentData.put("title", title);
                documentData.put("date", date);
                userAgreementRequest.setDocumentData(documentData);
                ApiResponse<UserAgreementResponse> response = accountsApi.createHasAgreedWithHttpInfo(accountId, clickwrapId, userAgreementRequest);

                return response.getData().getAgreementUrl();
    }

    public static ClickwrapVersionsResponse getActiveClickwraps(
            AccountsApi accountsApi,
            String accountId
    ) throws ApiException{
        AccountsApi.GetClickwrapsOptions options = accountsApi.new GetClickwrapsOptions();
        options.setStatus("active");
        return accountsApi.getClickwraps(accountId, options);
    }
}
