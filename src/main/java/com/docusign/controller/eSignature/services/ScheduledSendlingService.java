package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.List;

public class ScheduledSendlingService {
    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;
    // document 1 (PDF) has sign here anchor tag /sn1/
    //
    // The envelope has one recipient.
    // recipient 1 - signer

    // create the envelope definition

    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String resumeDate,
            String status) throws IOException {
        // The DocuSign platform searches throughout your envelope's documents
        // for matching anchor strings.

        // Step 2 start
        //ds-snippet-start:eSign35Step2
        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));

        // Create a signer recipient to sign the document, identified by name
        // and email. We're setting the parameters via the object creation.
        // RoutingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signerTabs);

        // The order in the docs array determines the order in the envelope
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document");
        envelope.setDocuments(List.of(
                EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "1")));
        envelope.setRecipients(EnvelopeHelpers.createSingleRecipient(signer));

        // Add the workflow to schedule the envelope with a delay
        EnvelopeDelayRule delayRule = new EnvelopeDelayRule();

        delayRule.setResumeDate(resumeDate + "T00:00:00Z");

        ScheduledSending scheduledSending = new ScheduledSending();
        scheduledSending.setRules(List.of(delayRule));

        Workflow workflow = new Workflow();
        workflow.setScheduledSending(scheduledSending);
        envelope.setWorkflow(workflow);

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelope.setStatus(status);

        return envelope;
        //ds-snippet-end:eSign35Step2

        // Step 2 end
    }
}
