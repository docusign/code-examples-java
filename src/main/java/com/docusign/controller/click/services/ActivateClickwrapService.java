package com.docusign.controller.click.services;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapRequest;
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
        return accountsApi.updateClickwrapVersion(
                accountId,
                clickwrapId,
                clickwrapVersionNumber,
                clickwrapRequest);
        //ds-snippet-end:Click2Step4
    }

    public static ClickwrapVersionsResponse getClickwrapsByStatus(
            AccountsApi accountsApi,
            String accountId,
            String status
    ) throws ApiException {
        AccountsApi.GetClickwrapsOptions options = accountsApi.new GetClickwrapsOptions();
        options.setStatus(status);
        return accountsApi.getClickwraps(accountId, options);
    }
}
