package com.docusign.controller.eSignature.services;

import com.docusign.DSConfiguration;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class CfrEmbeddedSigningService {
    //ds-snippet-start:eSign41Step6
    public static ViewUrl embeddedSigning(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            RecipientViewRequest viewRequest
    ) throws ApiException {
        return envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
    }
    //ds-snippet-end:eSign41Step6

    //ds-snippet-start:eSign41Step5
    public static RecipientViewRequest makeRecipientViewRequest(
            String signerEmail,
            String signerName,
            DSConfiguration config,
            String clientUserId
    ) {
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        // The query parameter is included as an example of how
        // to save/recover state information during the redirect to
        // the DocuSign signing. It's usually better to use
        // the session mechanism of your web framework. Query parameters
        // can be changed/spoofed very easily.
        String stateValue = "?state=123";
        viewRequest.setReturnUrl(config.getDsReturnUrl() + stateValue);

        // How has your app authenticated the user? In addition to your app's
        // authentication, you can include authenticate steps from DocuSign.
        // Eg, SMS authentication
        String authenticationMethod = "none";
        viewRequest.setAuthenticationMethod(authenticationMethod);

        // Recipient information must match embedded recipient info
        // we used to create the envelope.
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(clientUserId);

        // DocuSign recommends that you redirect to DocuSign for the
        // embedded signing. There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign signing web page
        // (not the DocuSign server) to send pings via AJAX to your app.
        // NOTE: The pings will only be sent if the pingUrl is an https address
        String pingFrequency = "600";
        viewRequest.setPingFrequency(pingFrequency); // seconds
        viewRequest.setPingUrl(config.getDsPingUrl());

        return viewRequest;
    }
    //ds-snippet-end:eSign41Step5

    //ds-snippet-start:eSign41Step3
    public static EnvelopeDefinition makeEnvelope(
            String signerName,
            String signerEmail,
            String countryCode,
            String number,
            String workflowId,
            String signerClientId,
            Integer anchorOffsetY,
            Integer anchorOffsetX,
            String documentFileName,
            String documentName
    ) throws IOException {
        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient
        RecipientIdentityPhoneNumber phoneNumber = new RecipientIdentityPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setNumber(number);

        RecipientIdentityInputOption inputOption = new RecipientIdentityInputOption();
        inputOption.setName("phone_number_list");
        inputOption.setValueType("PhoneNumberList");
        inputOption.setPhoneNumberList(List.of(phoneNumber));

        RecipientIdentityVerification identityVerifcation = new RecipientIdentityVerification();

        identityVerifcation.setWorkflowId(workflowId);
        identityVerifcation.setInputOptions(List.of(inputOption));

        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(signerClientId);
        signer.recipientId("1");
        signer.setTabs(EnvelopeHelpers.createSingleSignerTab("/sn1/", anchorOffsetY, anchorOffsetX));
        signer.setIdentityVerification(identityVerifcation);

        // Add the recipient to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document");
        envelopeDefinition.setRecipients(recipients);
        Document doc = EnvelopeHelpers.createDocumentFromFile(documentFileName, documentName, "3");
        envelopeDefinition.setDocuments(Collections.singletonList(doc));
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
    }
    //ds-snippet-end:eSign41Step3
}
