package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
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

        RecipientIdentityVerification identityVerifcation = new RecipientIdentityVerification();

        identityVerifcation.setWorkflowId(workFlowId);
        identityVerifcation.setInputOptions(List.of(inputOption));

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setEmail(signerEmail);
        signer.setRoutingOrder("1");
        signer.setStatus(EnvelopeHelpers.SIGNER_STATUS_CREATED);
        signer.setDeliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL);
        signer.setRecipientId(signHere.getRecipientId());
        signer.setTabs(EnvelopeHelpers.createSignerTabs(signHere));
        signer.setIdentityVerification(identityVerifcation);

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
