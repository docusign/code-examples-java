package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public final class PauseSignatureWorkflowService {
    private static final String DOCUMENT_FILE_NAME = "Welcome.txt";

    private static final String DOCUMENT_ID = "1";

    private static final String DOCUMENT_NAME = "Welcome";

    public static EnvelopeSummary pauseSignatureWorkflow(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        return envelopesApi.createEnvelope(accountId, envelope);
    }

    //ds-snippet-start:eSign32Step3
    public static EnvelopeDefinition createEnvelope(
            String signerName,
            String signerEmail,
            String signerName2,
            String signerEmail2
    ) throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, DOCUMENT_ID);

        WorkflowStep workflowStep = new WorkflowStep();
        workflowStep.setAction(EnvelopeHelpers.WORKFLOW_STEP_ACTION_PAUSE);
        workflowStep.setTriggerOnItem(EnvelopeHelpers.WORKFLOW_TRIGGER_ROUTING_ORDER);
        workflowStep.setItemId("2");

        Workflow workflow = new Workflow();
        workflow.setWorkflowSteps(Collections.singletonList(workflowStep));

        SignHere signHere1 = new SignHere();
        signHere1.setName("SignHereTabs");
        signHere1.setXPosition("200");
        signHere1.setYPosition("200");
        signHere1.setTabLabel("Sign Here");
        signHere1.setPageNumber("1");
        signHere1.setDocumentId(document.getDocumentId());

        Signer signer1 = new Signer();
        signer1.setName(signerName);
        signer1.setEmail(signerEmail);
        signer1.setRoutingOrder("1");
        signer1.setRecipientId("1");
        signer1.setTabs(EnvelopeHelpers.createSignerTabs(signHere1));

        SignHere signHere2 = new SignHere();
        signHere2.setName("SignHereTabs");
        signHere2.setXPosition("300");
        signHere2.setYPosition("200");
        signHere2.setTabLabel("Sign Here");
        signHere2.setPageNumber("1");
        signHere2.setDocumentId(document.getDocumentId());

        Signer signer2 = new Signer();
        signer2.setName(signerName2);
        signer2.setEmail(signerEmail2);
        signer2.setRoutingOrder("2");
        signer2.setRecipientId("2");
        signer2.setTabs(EnvelopeHelpers.createSignerTabs(signHere2));

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer1, signer2));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("EnvelopeWorkflowTest");
        envelope.setDocuments(Collections.singletonList(document));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);
        envelope.setWorkflow(workflow);

        return envelope;
    }
    //ds-snippet-end:eSign32Step3
}
