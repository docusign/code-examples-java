package com.docusign.services.click.examples;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapAgreementsResponse;

public final class GetClickwrapResponsesService {
    public static ClickwrapAgreementsResponse getClickwrapResponses(
            AccountsApi accountsApi,
            String accountId,
            String clickwrapId) throws ApiException {
        // Step 3: Call the v1 Click API
        return accountsApi.getClickwrapAgreements(accountId, clickwrapId);
    }
}
