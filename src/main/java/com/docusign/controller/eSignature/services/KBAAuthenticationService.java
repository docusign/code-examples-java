package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;

public final class KBAAuthenticationService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem";

    public static EnvelopeSummary kbaAuthentication(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        //ds-snippet-start:eSign22Step4
        return envelopesApi.createEnvelope(accountId, envelope);
        //ds-snippet-end:eSign22Step4
    }

    //ds-snippet-start:eSign22Step3
    public static Signer setSignHereAndSignerForEnvelope(
            String signerName,
            String signerEmail) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");
        SignHere signHere = new SignHere();
        signHere.setName("SignHereTab");
        signHere.setXPosition("75");
        signHere.setYPosition("572");
        signHere.setTabLabel("SignHereTab");
        signHere.setPageNumber("1");
        signHere.setDocumentId(doc.getDocumentId());
        // A 1- to 8-digit integer or 32-character GUID to match recipient IDs on your own systems.
        // This value is referenced in the Tabs element below to assign tabs on a per-recipient basis.
        signHere.setRecipientId("1");

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setEmail(signerEmail);
        signer.setRoutingOrder("1");
        signer.setStatus(EnvelopeHelpers.SIGNER_STATUS_CREATED);
        signer.setDeliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL);
        signer.setRecipientId(signHere.getRecipientId());
        signer.setTabs(EnvelopeHelpers.createSignerTabs(signHere));

        return signer;
    }

    public static EnvelopeDefinition createEnvelope(
            String signerName,
            String signerEmail
    ) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");
        Signer signer = setSignHereAndSignerForEnvelope(signerName, signerEmail);
        signer.setIdCheckConfigurationName("ID Check");
        signer.setRequireIdLookup("true");

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please Sign");
        envelope.setEnvelopeIdStamping("true");
        envelope.setEmailBlurb("Sample text for email body");
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setDocuments(Collections.singletonList(doc));
        envelope.setRecipients(recipients);

        return envelope;
    }
    //ds-snippet-end:eSign22Step3
}
