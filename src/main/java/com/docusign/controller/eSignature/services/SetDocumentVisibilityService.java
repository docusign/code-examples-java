package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public final class SetDocumentVisibilityService {
    //ds-snippet-start:eSign40Step3  
    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String signerEmail2,
            String signerName2,
            String ccEmail,
            String ccName,
            String documentFileNamePDF,
            String documentFileNameDOCX,
            String documentFileNameHTML
    ) throws IOException {
        CarbonCopy carbonCopy = new CarbonCopy();

        carbonCopy.setEmail(ccEmail);
        carbonCopy.setName(ccName);
        carbonCopy.setRecipientId("3");
        carbonCopy.setRoutingOrder("2");

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(prepareSigners(signerEmail, signerName, signerEmail2, signerName2)));
        recipients.setCarbonCopies(Collections.singletonList(carbonCopy));

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document set");
        envelopeDefinition.setRecipients(recipients);

        envelopeDefinition.setDocuments(Arrays.asList(prepareDocumentsForSending(
                documentFileNamePDF,
                documentFileNameDOCX,
                documentFileNameHTML)));

        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
    }

    private static Signer[] prepareSigners(
            String signerEmail,
            String signerName,
            String signerEmail2,
            String signerName2) {
        Signer signer1 = new Signer();

        signer1.setEmail(signerEmail);
        signer1.setName(signerName);
        signer1.setRecipientId("1");
        signer1.setRoutingOrder("1");
        signer1.setExcludedDocuments(Arrays.asList("2", "3"));
        signer1.setTabs(EnvelopeHelpers.createSingleSignerTab("**signature_1**", 10, 20));

        Signer signer2 = new Signer();

        signer2.setEmail(signerEmail2);
        signer2.setName(signerName2);
        signer2.setRecipientId("2");
        signer2.setRoutingOrder("1");
        signer2.setExcludedDocuments(Arrays.asList("1"));
        signer2.setTabs(EnvelopeHelpers.createSingleSignerTab("/sn1/", 10, 20));

        return new Signer[]{signer1, signer2};
    }

    private static Document[] prepareDocumentsForSending(
            String documentFileNamePDF,
            String documentFileNameDOCX,
            String documentFileNameHTML) throws IOException {
        Document documentHTML = EnvelopeHelpers.createDocumentFromFile(
                documentFileNameHTML,
                "Order acknowledgement",
                "1");

        Document documentDOCX = EnvelopeHelpers.createDocumentFromFile(
                documentFileNameDOCX,
                "Battle Plan",
                "2");

        Document documentPDF = EnvelopeHelpers.createDocumentFromFile(
                documentFileNamePDF,
                "Lorem Ipsum",
                "3");

        return new Document[]{documentHTML, documentDOCX, documentPDF};
    }
    //ds-snippet-end:eSign40Step3
}
