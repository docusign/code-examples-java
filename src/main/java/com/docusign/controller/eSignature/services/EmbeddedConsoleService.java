package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.ConsoleViewRequest;
import com.docusign.esign.model.ViewUrl;

public final class EmbeddedConsoleService {
    public static ViewUrl createConsoleView(
            EnvelopesApi envelopesApi,
            String dsReturnUrl,
            String envelopeId,
            String startingView,
            String accountId) throws ApiException {
        // Step 1. Create the Console / Web UI view.
        // Set the URL where you want the recipient to go once they are finished in
        // the Web UI. There are cases where a user will never click "FINISH" within
        // the Web UI, you cannot assume control will be passed back to your application.
        //ds-snippet-start:eSign12Step2
        ConsoleViewRequest viewRequest = new ConsoleViewRequest();
        viewRequest.setReturnUrl(dsReturnUrl);
        if ("envelope".equalsIgnoreCase(startingView) && envelopeId != null) {
            viewRequest.setEnvelopeId(envelopeId);
        }

        // Step 2. Call the CreateSenderView API
        return envelopesApi.createConsoleView(accountId, viewRequest);
        //ds-snippet-end:eSign12Step2
    }
}
