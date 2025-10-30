package com.docusign.controller.workspaces.services;

import java.util.Arrays;

import org.springframework.http.HttpHeaders;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeUpdateSummary;
import com.docusign.esign.model.Signer;
import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.WorkspaceEnvelopeForCreate;
import com.docusign.iam.sdk.models.operations.CreateWorkspaceEnvelopeResponse;

public final class SendEnvelopeWithRecipientInfoService {
    private static final String ENVELOPE_NAME = "Example Workspace Envelope";
    private static final String EMAIL_SUBJECT = "Please sign this document";
    private static final String ANCHOR_STRING = "/sn1/";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;
    
    private static IamClient createIamClient(String accessToken) {
        return IamClient.builder().accessToken(accessToken).build();
    }

    public static CreateWorkspaceEnvelopeResponse createWorkspaceEnvelope(
            String accessToken,
            String accountId,
            String workspaceId,
            String documentId) throws Exception {

        var workspaceEnvelopeForCreate = new WorkspaceEnvelopeForCreate();
        workspaceEnvelopeForCreate.withEnvelopeName(ENVELOPE_NAME);
        workspaceEnvelopeForCreate.withDocumentIds(Arrays.asList(documentId));
        
        return createIamClient(accessToken)
            .workspaces()
            .workspaces()
            .createWorkspaceEnvelope()
            .accountId(accountId)
            .workspaceId(workspaceId)
            .workspaceEnvelopeForCreate(workspaceEnvelopeForCreate)
            .call();
    }

    public static EnvelopeUpdateSummary sendEnvelope(
        String accessToken,
        String basePath,
        String accountId,
        String envelopeId,
        String signerEmail,
        String signerName
    ) throws ApiException {
        var client = new ApiClient(basePath);
        client.addDefaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        var envelopesApi = new EnvelopesApi(client);

        var envelope = makeEnvelope(signerEmail, signerName);

        return envelopesApi.update(accountId, envelopeId, envelope);
    }

    private static Envelope makeEnvelope(
        String signerEmail,
        String signerName
    ) {
        var signerTabs = EnvelopeHelpers.createSignerTabs(EnvelopeHelpers.createSignHere(ANCHOR_STRING, ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));

        var signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signerTabs);

        var envelope = new Envelope();
        envelope.setRecipients(EnvelopeHelpers.createSingleRecipient(signer));
        envelope.setEmailSubject(EMAIL_SUBJECT);
        envelope.setStatus("sent");

        return envelope;
    }
}
