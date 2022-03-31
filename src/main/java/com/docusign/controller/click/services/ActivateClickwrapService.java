package com.docusign.controller.click.services;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.controller.click.examples.ClickwrapHelper;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.click.api.AccountsApi.GetClickwrapsOptions;

public final class ActivateClickwrapService {
    public static ClickwrapVersionSummaryResponse activateClickwrap(
            AccountsApi accountsApi,
            String accountId,
            String clickwrapId,
            String clickwrapVersionNumber) throws ApiException {
        // Step 3: Construct the request body for your clickwrap
        ClickwrapRequest clickwrapRequest = new ClickwrapRequest().status(ClickwrapHelper.STATUS_ACTIVE);

        // Step 4: Call the v1 Click API
        return accountsApi.updateClickwrapVersion(
                accountId,
                clickwrapId,
                clickwrapVersionNumber,
                clickwrapRequest);
    }

    public static ClickwrapVersionsResponse getInactiveClickwraps(
            AccountsApi accountsApi,
            String accountId
    ) throws ApiException{
        AccountsApi.GetClickwrapsOptions options = accountsApi.new GetClickwrapsOptions();
        options.setStatus("inactive");
        return accountsApi.getClickwraps(accountId, options);
    }
}
