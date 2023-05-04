package com.docusign.controller.click.services;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.click.model.DisplaySettings;
import com.docusign.click.model.Document;
import com.docusign.controller.click.examples.ClickwrapHelper;

import java.io.IOException;

public final class CreateClickwrapService {
    //ds-snippet-start:Click1Step3
    public static ClickwrapRequest createClickwrapRequest(
            String clickwrapName,
            String fileName,
            String documentName,
            Integer documentOrder
    ) throws IOException {
        Document document = ClickwrapHelper.createDocumentFromFile(fileName, documentName, documentOrder);
        DisplaySettings displaySettings = new DisplaySettings()
                .displayName("Terms of Service")
                .consentButtonText("I Agree")
                .downloadable(true)
                .format("modal")
                .mustRead(true)
                .requireAccept(true)
                .documentDisplay("document");

        return new ClickwrapRequest()
                .addDocumentsItem(document)
                .clickwrapName(clickwrapName)
                .requireReacceptance(true)
                .displaySettings(displaySettings);
    }
    //ds-snippet-end:Click1Step3

    public static ClickwrapVersionSummaryResponse createClickwrap(
            AccountsApi accountsApi,
            String accountId,
            ClickwrapRequest clickwrapRequest
    ) throws IOException, ApiException {
        return accountsApi.createClickwrap(
                accountId,
                clickwrapRequest);
    }
}
