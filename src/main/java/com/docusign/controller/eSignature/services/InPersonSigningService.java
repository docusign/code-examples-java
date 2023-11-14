package com.docusign.controller.eSignature.services;

import com.docusign.DSConfiguration;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;

public final class InPersonSigningService {
    public static ViewUrl inPersonSigning(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            RecipientViewRequest viewRequest
    ) throws ApiException {
        return envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
    }

    //ds-snippet-start:eSign39Step4
    public static RecipientViewRequest makeRecipientViewRequest(
            String hostEmail,
            String hostName,
            DSConfiguration config) {
        String stateValue = "?state=123";
        String authenticationMethod = "none";
        String pingFrequency = "600";

        RecipientViewRequest viewRequest = new RecipientViewRequest();

        viewRequest.setReturnUrl(config.getDsReturnUrl() + stateValue);
        viewRequest.setAuthenticationMethod(authenticationMethod);
        viewRequest.setEmail(hostEmail);
        viewRequest.setUserName(hostName);
        viewRequest.setPingFrequency(pingFrequency);
        viewRequest.setPingUrl(config.getDsPingUrl());

        return viewRequest;
    }
    //ds-snippet-end:eSign39Step4

    //ds-snippet-start:eSign39Step2
    public static EnvelopeDefinition makeEnvelope(
            String hostEmail,
            String hostName,
            String signerName,
            Integer anchorOffsetY,
            Integer anchorOffsetX,
            String documentFileName,
            String documentName
    ) throws IOException {
        InPersonSigner inPersonSigner = new InPersonSigner();

        inPersonSigner.setHostEmail(hostEmail);
        inPersonSigner.setHostName(hostName);
        inPersonSigner.setSignerName(signerName);
        inPersonSigner.setRecipientId("1");
        inPersonSigner.setRoutingOrder("1");
        inPersonSigner.setTabs(EnvelopeHelpers.createSingleSignerTab("/sn1/", anchorOffsetY, anchorOffsetX));

        Recipients recipients = new Recipients();
        recipients.setInPersonSigners(Collections.singletonList(inPersonSigner));

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please host this in-person signing session");
        envelopeDefinition.setRecipients(recipients);

        Document document = EnvelopeHelpers.createDocumentFromFile(documentFileName, documentName, "1");
        envelopeDefinition.setDocuments(Collections.singletonList(document));
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
    }
    //ds-snippet-end:eSign39Step2
}
