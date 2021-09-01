package com.docusign.services.click.examples;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.click.model.DisplaySettings;
import com.docusign.click.model.Document;
import com.docusign.controller.click.examples.ClickwrapHelper;

import java.io.IOException;

public final class CreateNewVersionClickwrapService {
    public static ClickwrapVersionSummaryResponse createNewVersionClickwrap(
            AccountsApi accountsApi,
            ClickwrapRequest clickwrapRequest,
            String accountId,
            String clickwrapId) throws ApiException {
        // Step 4: Call the v1 Click API
        return accountsApi.createClickwrapVersion(
                accountId,
                clickwrapId,
                clickwrapRequest);
    }

    public static ClickwrapRequest createClickwrapRequest(
            String fileName,
            String documentName,
            Integer documentOrder) throws IOException {
        Document document = ClickwrapHelper.createDocumentFromFile(fileName, documentName, documentOrder);
        DisplaySettings displaySettings = new DisplaySettings()
                .displayName("Terms of Service v2")
                .consentButtonText("I Agree")
                .downloadable(true)
                .format("modal")
                .mustRead(true)
                .mustView(true)
                .requireAccept(true)
                .documentDisplay("document");

        return new ClickwrapRequest()
                .addDocumentsItem(document)
                .clickwrapName("Terms of Service")
                .requireReacceptance(true)
                .status(ClickwrapHelper.STATUS_ACTIVE)
                .displaySettings(displaySettings);
    }
}
