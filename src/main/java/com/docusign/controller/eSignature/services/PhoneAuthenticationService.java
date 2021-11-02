package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class PhoneAuthenticationService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "Lorem";

    public static EnvelopeSummary phoneAuthentication(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        return envelopesApi.createEnvelope(accountId, envelope);
    }

    public static EnvelopeDefinition createEnvelope(
            String signerName,
            String signerEmail,
            String phoneNumber
    ) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        Signer signer = KBAAuthenticationService.setSignHereAndSignerForEnvelope(signerName, signerEmail);

        List<String> RECIPIENT_PHONE_NUMBERS = List.of(phoneNumber);
        RecipientPhoneAuthentication phoneAuth = new RecipientPhoneAuthentication();
        phoneAuth.setRecipMayProvideNumber("true");
        phoneAuth.setSenderProvidedNumbers(RECIPIENT_PHONE_NUMBERS);

        signer.setRequireIdLookup("true");
        signer.setPhoneAuthentication(phoneAuth);
        signer.setIdCheckConfigurationName("Phone Auth $");

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please Sign");
        envelope.setEnvelopeIdStamping("true");
        envelope.setEmailBlurb("Sample text for email body");
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);
        envelope.setDocuments(Collections.singletonList(doc));

        return envelope;
    }
}
