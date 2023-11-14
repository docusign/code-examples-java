package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;

public final class AccessCodeAuthenticationService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem";

    public static EnvelopeSummary accessCodeAuthentication(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
       //ds-snippet-start:eSign19Step4
        return envelopesApi.createEnvelope(accountId, envelope);
       //ds-snippet-end:eSign19Step4
    }

    //ds-snippet-start:eSign19Step3
    public static EnvelopeDefinition createEnvelope(
            String signerName,
            String signerEmail,
            String accessCode
    ) throws IOException {
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
