package com.docusign.services.eSignature.examples;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeUpdateSummary;
import com.docusign.esign.model.Workflow;

public class UnpauseSignatureWorkflowService {
    public static EnvelopeUpdateSummary unpauseSignatureWorkflow(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        // Step 3: Construct your envelope JSON body
        Workflow workflow = new Workflow();
        workflow.setWorkflowStatus(EnvelopeHelpers.WORKFLOW_STATUS_IN_PROGRESS);

        Envelope envelope = new Envelope();
        envelope.setWorkflow(workflow);

        EnvelopesApi.UpdateOptions updateOptions = envelopesApi.new UpdateOptions();
        updateOptions.setResendEnvelope("true");

        // Step 4: Call the eSignature REST API
        return envelopesApi.update(
                accountId,
                envelopeId,
                envelope,
                updateOptions
        );
    }
}
