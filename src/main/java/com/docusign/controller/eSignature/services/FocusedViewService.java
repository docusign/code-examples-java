package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ViewUrl;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class FocusedViewService {

    private static String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static String DOCUMENT_NAME = "Lorem Ipsum";

    private static String SIGNER_CLIENT_ID = "1000";

    private static int ANCHOR_OFFSET_Y = 20;

    private static int ANCHOR_OFFSET_X = 10;

    public static String STATE_123 = "?state=123";

    public static String AUTHENTICATION_METHOD = "none";

    public String[] sendEnvelopeWithFocusedView(
            String signerEmail,
            String signerName,
            ApiClient apiClient,
            String accountId,
            String returnUrl
    ) throws ApiException, IOException {
        //ds-snippet-start:eSign44Step3
        EnvelopeDefinition envelope = makeEnvelope(
                signerEmail,
                signerName,
                SIGNER_CLIENT_ID,
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME);

        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelope);

        String envelopeId = envelopeSummary.getEnvelopeId();
        //ds-snippet-end:eSign44Step3

        //ds-snippet-start:eSign44Step5
        RecipientViewRequest viewRequest = makeRecipientViewRequest(signerEmail, signerName, returnUrl, SIGNER_CLIENT_ID, returnUrl);
        ViewUrl viewUrl = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
        String redirectUrl = viewUrl.getUrl();

        return new String[] { envelopeId, redirectUrl };
        //ds-snippet-end:eSign44Step5
    }

    //ds-snippet-start:eSign44Step4
    public RecipientViewRequest makeRecipientViewRequest(
            String signerEmail,String signerName, String returnUrl, String signerClientId, String pingUrl
    ) throws ApiException {
        String pingFrequency = "600";
        String linkToLauncher = "http://localhost:8080";
        String linkToDocuSignServer = "https://apps-d.docusign.com";

        RecipientViewRequest viewRequest = new RecipientViewRequest();

        viewRequest.setReturnUrl(returnUrl + STATE_123);
        viewRequest.setAuthenticationMethod(AUTHENTICATION_METHOD);
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(signerClientId);

        if (pingUrl != null)
        {
            viewRequest.setPingFrequency(pingFrequency);
            viewRequest.setPingUrl(pingUrl);
        }

        viewRequest.setFrameAncestors(Arrays.asList(new String[]{linkToLauncher, linkToDocuSignServer}));
        viewRequest.setMessageOrigins(Arrays.asList(new String[]{linkToDocuSignServer}));

        return viewRequest;
    }
    //ds-snippet-end:eSign44Step4

    //ds-snippet-start:eSign44Step2
    public EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String signerClientId,
            Integer anchorOffsetY,
            Integer anchorOffsetX,
            String documentFileName,
            String documentName
    ) throws IOException {
        String emailSubject = "Please sign this document";
        String recipientId = "1";
        String anchorString = "/sn1/";
        String docId = "3";

        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(signerClientId);
        signer.recipientId(recipientId);
        signer.setTabs(EnvelopeHelpers.createSingleSignerTab(anchorString, anchorOffsetY, anchorOffsetX));

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject(emailSubject);
        envelopeDefinition.setRecipients(recipients);

        Document document = EnvelopeHelpers.createDocumentFromFile(documentFileName, documentName, docId);
        envelopeDefinition.setDocuments(Collections.singletonList(document));
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
    }
    //ds-snippet-end:eSign44Step2
}
