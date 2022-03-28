package com.docusign.controller.eSignature.services;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.ReturnUrlRequest;
import com.docusign.esign.model.ViewUrl;

import java.io.IOException;

public final class EmbeddedSendingService {
    public static ViewUrl createSenderView(
        EnvelopesApi envelopesApi,
        String accountId,
        String envelopeId,
        String dsReturnUrl
    ) throws ApiException {
        // Step 2. Create the sender view.
        // Set the url where you want the recipient to go once they are done
        // signing should typically be a callback route somewhere in your app.
        ReturnUrlRequest viewRequest = new ReturnUrlRequest();
        viewRequest.setReturnUrl(dsReturnUrl);
        return envelopesApi.createSenderView(accountId, envelopeId, viewRequest);
    }

    public static EnvelopeSummary createEnvelopeWithDraftStatus(
        EnvelopesApi envelopesApi,
        String signerEmail,
        String signerName,
        String ccEmail,
        String ccName,
        String status,
        WorkArguments args,
        String accountId
    ) throws IOException, ApiException {
        args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);
        EnvelopeDefinition env = SigningViaEmailService.makeEnvelope(
            signerEmail,
            signerName,
            ccEmail,
            ccName,
            status,
            args);
        return envelopesApi.createEnvelope(accountId, env);
    }
}
