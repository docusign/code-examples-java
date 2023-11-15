package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public final class IdvAuthenticationService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    public static EnvelopeSummary idvAuthentication(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        return envelopesApi.createEnvelope(accountId, envelope);
    }

    public static String retrieveWorkflowId(ApiClient apiClient, String accountId) throws ApiException {
        //ds-snippet-start:eSign23Step3
        AccountsApi workflowDetails = new AccountsApi(apiClient);
        AccountIdentityVerificationResponse workflowRes = workflowDetails.getAccountIdentityVerification(accountId);
        List<AccountIdentityVerificationWorkflow> identityVerification = workflowRes.getIdentityVerification();
        String workflowId = "";
        for (int i = 0; i < identityVerification.size(); i++) {
            if (identityVerification.get(i).getDefaultName().equals("DocuSign ID Verification")) {
                workflowId = identityVerification.get(i).getWorkflowId();
            }
        }
        return  workflowId;
        //ds-snippet-end:eSign23Step3
    }

    //ds-snippet-start:eSign23Step4
    public static EnvelopeDefinition createEnvelope(
            String signerName,
            String signerEmail,
            String workflowId
    ) {
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign");
        envelopeDefinition.setEmailBlurb("Sample text for email body");
        envelopeDefinition.setStatus("Sent");

        byte[] fileBytes = new byte[0];
        try {
            fileBytes = EnvelopeHelpers.readFile(DOCUMENT_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc1 = new Document();
        doc1.setDocumentBase64(Base64.getEncoder().encodeToString(fileBytes));
        doc1.setDocumentId("1");
        doc1.setFileExtension("pdf");
        doc1.setName("Lorem");

        SignHere signHere1 = new SignHere();
        signHere1.setName("SignHereTab");
        signHere1.setXPosition("75");
        signHere1.setYPosition("572");
        signHere1.setTabLabel("SignHereTab");
        signHere1.setPageNumber("1");
        signHere1.setDocumentId("1");
        // A 1- to 8-digit integer or 32-character GUID to match recipient IDs on your own systems.
        // This value is referenced in the Tabs element below to assign tabs on a per-recipient basis.
        signHere1.setRecipientId("1"); //represents your {RECIPIENT_ID}

        Tabs signer1Tabs = new Tabs();
        signer1Tabs.setSignHereTabs(Collections.singletonList(signHere1));
        RecipientIdentityVerification workflow = new RecipientIdentityVerification();
        workflow.setWorkflowId(workflowId);

        String status = "created";
        String deliveryMethod = "email";
        Signer signer1 = new Signer();
        signer1.setName(signerName);
        signer1.setEmail(signerEmail);
        signer1.setRoleName("");
        signer1.setNote("");
        signer1.setStatus(status);
        signer1.setDeliveryMethod(deliveryMethod);
        signer1.setRecipientId("1"); //represents your {RECIPIENT_ID}
        signer1.setIdentityVerification(workflow);
        signer1.setTabs(signer1Tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer1));

        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setDocuments(Collections.singletonList(doc1));
        return envelopeDefinition;
    }
    //ds-snippet-end:eSign23Step4
}
