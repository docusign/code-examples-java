package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.time.Instant;
import java.util.Map;

public final class AccessCodeAuthenticationService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem";

    public static EnvelopeSummary accessCodeAuthentication(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope) throws ApiException {
        //ds-snippet-start:eSign19Step4
        var createdEnvelope = envelopesApi.createEnvelopeWithHttpInfo(accountId, envelope,
                envelopesApi.new CreateEnvelopeOptions());

        Map<String, List<String>> headers = createdEnvelope.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return createdEnvelope.getData();
        //ds-snippet-end:eSign19Step4
    }

    //ds-snippet-start:eSign19Step3
    public static EnvelopeDefinition createEnvelope(
            String signerName,
            String signerEmail,
            String accessCode) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        Signer signer = KBAAuthenticationService.setSignHereAndSignerForEnvelope(signerName, signerEmail);
        signer.setAccessCode(accessCode);

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please Sign");
        envelope.setDocuments(Collections.singletonList(doc));
        envelope.setEnvelopeIdStamping("true");
        envelope.setEmailBlurb("Sample text for email body");
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);

        return envelope;
    }
    //ds-snippet-end:eSign19Step3
}
