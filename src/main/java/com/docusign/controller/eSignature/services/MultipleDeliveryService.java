package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class MultipleDeliveryService {
    private static final String EMAIL_DELIVERY = "Email";

    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    
    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";

    private static final int ANCHOR_OFFSET_Y = 10;
    
    private static final int ANCHOR_OFFSET_X = 20;

    private static final String SIGNER_ID = "1";
    
    private static final String CC_ID = "2";

    public static EnvelopeSummary smsDelivery(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope) throws ApiException {
        var createEnvelopeResponse = envelopesApi.createEnvelopeWithHttpInfo(
            accountId,
            envelope,
            envelopesApi.new CreateEnvelopeOptions()
        );

        Map<String, List<String>> headers = createEnvelopeResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return createEnvelopeResponse.getData();
    }

    //ds-snippet-start:eSign46Step2
    public static EnvelopeDefinition makeEnvelope(
        String signerName,
        String signerEmail,
        String countryCode,
        String signersPhoneNumber,
        String ccName,
        String ccEmail,
        String ccCountryCode,
        String carbonCopyPhoneNumber,
        String deliveryMethod,
        String status) throws IOException {
        
        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
            EnvelopeHelpers.createSignHere(
            "/sn1/",
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X
            ));

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setEmail(signerEmail);
        signer.setRecipientId(SIGNER_ID);
        signer.setRoutingOrder("1");
        signer.setTabs(signerTabs);
        signer.setDeliveryMethod(EMAIL_DELIVERY);
        signer.setAdditionalNotifications(Arrays.asList(
            createAdditionalNotification(countryCode, signersPhoneNumber, deliveryMethod)
        ));

        CarbonCopy cc = new CarbonCopy();
        cc.setName(ccName);
        cc.setEmail(ccEmail);
        cc.setRecipientId(CC_ID);
        cc.setRoutingOrder("2");
        cc.setDeliveryMethod(EMAIL_DELIVERY);
        cc.setAdditionalNotifications(Arrays.asList(
            createAdditionalNotification(ccCountryCode, carbonCopyPhoneNumber, deliveryMethod)
        ));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setDocuments(
            Arrays.asList(
                EnvelopeHelpers.createDocumentFromFile(
                    DOCX_DOCUMENT_FILE_NAME,
                    DOCX_DOCUMENT_NAME,
                    "2"
                ),
                EnvelopeHelpers.createDocumentFromFile(
                    PDF_DOCUMENT_FILE_NAME,
                    PDF_DOCUMENT_NAME,
                    "3"
                )
            )
        );
        envelope.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        envelope.setStatus(status);

        return envelope;
    }

    private static RecipientAdditionalNotification createAdditionalNotification(
            String countryCode,
            String phoneNumber,
            String deliveryMethod) {

        RecipientPhoneNumber phone = new RecipientPhoneNumber();
        phone.setCountryCode(countryCode);
        phone.setNumber(phoneNumber);

        RecipientAdditionalNotification notification = new RecipientAdditionalNotification();
        notification.setSecondaryDeliveryMethod(deliveryMethod);
        notification.setPhoneNumber(phone);

        return notification;
    }
    //ds-snippet-end:eSign46Step2
}
