package com.docusign.controller.eSignature.services;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;

public final class ResponsiveSigningService {
    //ds-snippet-start:eSign38Step2
    private static final String HTML_DOCUMENT_FILE_NAME = "templates/order-form.ftl";
    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String signerClientId,
            String ccEmail,
            String ccName,
            WorkArguments args
    ) throws IOException {
        String htmlDoc = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args);
        htmlDoc = htmlDoc.replace("/sn1/", "<ds-signature data-ds-role=\"Signer\"/>")
                .replace("/l1q/", " <input data-ds-type=\"number\"/>")
                .replace("/l2q/", " <input data-ds-type=\"number\"/>");;

        Signer signer = new Signer()
                .email(signerEmail)
                .name(signerName)
                .clientUserId(signerClientId)
                .recipientId("1")
                .routingOrder("1")
                .roleName("Signer");

        CarbonCopy cc = new CarbonCopy()
                .email(ccEmail)
                .name(ccName)
                .recipientId("2")
                .routingOrder("1");

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Example Signing Document");
        envelope.setDocuments(Collections.singletonList(
                new Document()
                        .name("Lorem Ipsum")
                        .documentId("1")
                        .htmlDefinition(new DocumentHtmlDefinition().source(htmlDoc))));
        envelope.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelope;
    }

    public static ViewUrl responsiveSigning(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            RecipientViewRequest viewRequest
    ) throws ApiException {
        return envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
    }

    public static RecipientViewRequest makeRecipientViewRequest(
            String signerEmail,
            String signerName,
            DSConfiguration config,
            String clientUserId
    ) {
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        String stateValue = "?state=123";
        viewRequest.setReturnUrl(config.getDsReturnUrl() + stateValue);

        String authenticationMethod = "none";
        viewRequest.setAuthenticationMethod(authenticationMethod);

        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(clientUserId);

        String pingFrequency = "600";
        viewRequest.setPingFrequency(pingFrequency);
        viewRequest.setPingUrl(config.getDsPingUrl());

        return viewRequest;
    }
    //ds-snippet-end:eSign38Step2
}
