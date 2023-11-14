package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DelayedRoutingService {
    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";

    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    private static final int ANCHOR_OFFSET_X_2 = 120;

    // document 1 (docx) has tag /sn1/
    // document 2 (pdf) has tag /sn1/
    //
    // The envelope has two recipients.
    // recipient 1 - signer
    // recipient 2 - cc
    // The envelope will be sent first to the signer. After it is signed,
    // a copy is sent to the cc person.
    public static EnvelopeDefinition makeEnvelope(
            String signerName,
            String signerEmail,
            String signerName2,
            String signerEmail2,
            String delay,
            String status) throws IOException {
        // The DocuSign platform searches throughout your envelope's documents
        // for matching anchor strings. So the signHere2 tab will be used in
        // both document 2 and 3 since they use the same anchor string for
        // their "signer 1" tabs.

        //ds-snippet-start:eSign36Step2
        Tabs signer1Tabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));

        Tabs signer2Tabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X_2));

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
        signer.setTabs(signer1Tabs);

        Signer signer2 = new Signer();
        signer2.setEmail(signerEmail2);
        signer2.setName(signerName2);
        signer2.setRecipientId("2");
        signer2.setRoutingOrder("2");
        signer2.setTabs(signer2Tabs);

        // The order in the docs array determines the order in the envelope
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocumentFromFile(DOCX_DOCUMENT_FILE_NAME, DOCX_DOCUMENT_NAME, "2"),
                EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "3")));
        envelope.setRecipients(EnvelopeHelpers.createTwoSigners(signer, signer2));

        // Add the workflow to delay the second recipient

        String delayFormatted = delay + ":00:00";
        EnvelopeDelayRule rule = new EnvelopeDelayRule();
        rule.setDelay(delayFormatted);

        DelayedRouting delayedRouting = new DelayedRouting();
        delayedRouting.setRules(List.of(rule));

        WorkflowStep workflowStep = new WorkflowStep();
        workflowStep.setAction("pause_before");
        workflowStep.setTriggerOnItem("routing_order");
        workflowStep.setItemId("2");
        workflowStep.setStatus("pending");
        workflowStep.setDelayedRouting(delayedRouting);

        Workflow workflow = new Workflow();
        workflow.setWorkflowSteps(List.of(workflowStep));
        envelope.setWorkflow(workflow);

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelope.setStatus(status);

        return envelope;
        //ds-snippet-end:eSign36Step2

    }
}
