package com.docusign.controller.click.services;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionsResponse;

public final class GetListClickwrapsService {
    public static ClickwrapVersionsResponse getListClickwrap(
            AccountsApi accountsApi,
            String accountId
    ) throws ApiException {
        // Step 3: Call the v1 Click API
        //ds-snippet-start:Click4Step3
        return accountsApi.getClickwraps(accountId);
        //ds-snippet-end:Click4Step3
    }
}
