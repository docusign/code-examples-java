package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;

public final class SMSDeliveryService {
    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";

    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    //ds-snippet-start:eSign37Step3
    public static EnvelopeSummary smsDelivery(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        return envelopesApi.createEnvelope(accountId, envelope);
    }
    //ds-snippet-end:eSign37Step3

    //ds-snippet-start:eSign37Step2
    // document 2 (docx) has tag /sn1/
    // document 3 (pdf) has tag /sn1/
    //
    // The envelope has two recipients.
    // recipient 1 - signer
    // recipient 2 - cc
    // The envelope will be sent first to the signer. After it is signed,
    // a copy is sent to the cc person.
    public static EnvelopeDefinition makeEnvelope(
            String countryCode,
            String signersPhoneNumber,
            String signerName,
            String deliveryMethod,
            String ccCountryCode,
            String carbonCopyPhoneNumber,
            String ccName,
            String status) throws IOException {
        // The DocuSign platform searches throughout your envelope's documents
        // for matching anchor strings. So the signHere2 tab will be used in
        // both document 2 and 3 since they use the same anchor string for
        // their "signer 1" tabs.
        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));

        // Create a signer recipient to sign the document, identified by name
        // and phone number. We're setting the parameters via the object creation.
        // RoutingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.

        RecipientPhoneNumber phoneNumber = new RecipientPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setNumber(signersPhoneNumber);

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setPhoneNumber(phoneNumber);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signerTabs);
        signer.setDeliveryMethod(deliveryMethod);

        // create a cc recipient to receive a copy of the documents, identified by name and phone number
        RecipientPhoneNumber ccPhoneNumber = new RecipientPhoneNumber();
        ccPhoneNumber.setCountryCode(ccCountryCode);
        ccPhoneNumber.setNumber(carbonCopyPhoneNumber);

        CarbonCopy cc = new CarbonCopy();
        cc.setName(ccName);
        cc.setPhoneNumber(ccPhoneNumber);
        cc.setRecipientId("2");
        cc.setRoutingOrder("2");
        cc.setDeliveryMethod(deliveryMethod);

        // The order in the docs array determines the order in the envelope
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocumentFromFile(DOCX_DOCUMENT_FILE_NAME, DOCX_DOCUMENT_NAME, "2"),
                EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "3")));
        envelope.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelope.setStatus(status);

        return envelope;
        //ds-snippet-end:eSign37Step2
    }
}
