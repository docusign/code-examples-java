package com.docusign.controller.click.services;

import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.client.ApiResponse;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.click.model.DisplaySettings;
import com.docusign.click.model.Document;
import com.docusign.controller.click.examples.ClickwrapHelper;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class CreateClickwrapService {
    //ds-snippet-start:Click1Step3
    public static ClickwrapRequest createClickwrapRequest(
            String clickwrapName,
            String fileName,
            String documentName,
            Integer documentOrder
    ) throws IOException {
        Document document = new ClickwrapHelper().createDocumentFromFile(fileName, documentName, documentOrder);
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
        ApiResponse<ClickwrapVersionSummaryResponse> response = accountsApi.createClickwrapWithHttpInfo(
                accountId,
                clickwrapRequest);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
    }
}
