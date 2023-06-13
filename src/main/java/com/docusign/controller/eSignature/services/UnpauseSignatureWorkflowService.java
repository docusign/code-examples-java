package com.docusign.controller.eSignature.services;

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
        //ds-snippet-start:eSign33Step3
        Workflow workflow = new Workflow();
        workflow.setWorkflowStatus(EnvelopeHelpers.WORKFLOW_STATUS_IN_PROGRESS);

        Envelope envelope = new Envelope();
        envelope.setWorkflow(workflow);

        EnvelopesApi.UpdateOptions updateOptions = envelopesApi.new UpdateOptions();
        updateOptions.setResendEnvelope("true");
        //ds-snippet-end:eSign33Step3

        // Step 4: Call the eSignature REST API
        //ds-snippet-start:eSign33Step4
        return envelopesApi.update(
                accountId,
                envelopeId,
                envelope,
                updateOptions
        );
        //ds-snippet-end:eSign33Step4
    }
}
