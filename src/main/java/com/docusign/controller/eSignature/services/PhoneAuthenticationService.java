package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class PhoneAuthenticationService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem";

    public static EnvelopeSummary phoneAuthentication(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        var envelopeResponse = envelopesApi.createEnvelopeWithHttpInfo(accountId, envelope, envelopesApi.new CreateEnvelopeOptions());

        Map<String, List<String>> headers = envelopeResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return envelopeResponse.getData();
    }

    //ds-snippet-start:eSign20Step4
    public static EnvelopeDefinition createEnvelope(String signerName, String signerEmail, String countryCode,
                                                    String phone, String workFlowId) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        SignHere signHere = new SignHere();
        signHere.setName("SignHereTab");
        signHere.setXPosition("200");
        signHere.setYPosition("160");
        signHere.setTabLabel("SignHereTab");
        signHere.setPageNumber("1");
        signHere.setDocumentId(doc.getDocumentId());
        // A 1- to 8-digit integer or 32-character GUID to match recipient IDs on your
        // own systems.
        // This value is referenced in the Tabs element below to assign tabs on a
        // per-recipient basis.
        signHere.setRecipientId("1");

        RecipientIdentityPhoneNumber phoneNumber = new RecipientIdentityPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setNumber(phone);

        RecipientIdentityInputOption inputOption = new RecipientIdentityInputOption();
        inputOption.setName("phone_number_list");
        inputOption.setValueType("PhoneNumberList");
        inputOption.setPhoneNumberList(List.of(phoneNumber));

        RecipientIdentityVerification identityVerification = new RecipientIdentityVerification();

        identityVerification.setWorkflowId(workFlowId);
        identityVerification.setInputOptions(List.of(inputOption));

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setEmail(signerEmail);
        signer.setRoutingOrder("1");
        signer.setStatus(EnvelopeHelpers.SIGNER_STATUS_CREATED);
        signer.setDeliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL);
        signer.setRecipientId(signHere.getRecipientId());
        signer.setTabs(EnvelopeHelpers.createSignerTabs(signHere));
        signer.setIdentityVerification(identityVerification);

        Recipients recipients = new Recipients();
        recipients.setSigners(List.of(signer));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please Sign");
        envelope.setDocuments(List.of(doc));
        envelope.setEnvelopeIdStamping("true");
        envelope.setEmailBlurb("Sample text for email body");
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);

        return envelope;
    }
    //ds-snippet-end:eSign20Step4
}
