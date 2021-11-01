package com.docusign.services.click.examples;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionsResponse;

public final class GetListClickwrapsService {
    public static ClickwrapVersionsResponse getListClickwrap(
            AccountsApi accountsApi,
            String accountId
    ) throws ApiException{
        // Step 3: Call the v1 Click API
        return accountsApi.getClickwraps(accountId);
    }
}
