package com.docusign.controller.click.services;

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
            String clickwrapId
    ) throws ApiException {
        // Step 4: Call the v1 Click API
        //ds-snippet-start:Click3Step4
        return accountsApi.createClickwrapVersion(
                accountId,
                clickwrapId,
                clickwrapRequest);
        //ds-snippet-end:Click3Step4
    }

    //ds-snippet-start:Click3Step3
    public static ClickwrapRequest createClickwrapRequest(
            String fileName,
            String documentName,
            Integer documentOrder,
            String clickwrapName
    ) throws IOException {
        Document document = ClickwrapHelper.createDocumentFromFile(fileName, documentName, documentOrder);
        DisplaySettings displaySettings = new DisplaySettings()
                .displayName(clickwrapName)
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
                .status(ClickwrapHelper.STATUS_ACTIVE)
                .displaySettings(displaySettings);
    }
    //ds-snippet-end:Click3Step3
}
