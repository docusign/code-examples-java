package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ApplyBrandToEnvelopeService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "EG025 Lorem Ipsum";

    private static final int ANCHOR_OFFSET_Y = 10;

    private static final int ANCHOR_OFFSET_X = 20;

    //ds-snippet-start:eSign29Step4
    public static EnvelopeSummary applyBrandToEnvelope(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        var createEnvelopeResponse = envelopesApi.createEnvelopeWithHttpInfo(accountId, envelope, envelopesApi.new CreateEnvelopeOptions());
        Map<String, List<String>> headers = createEnvelopeResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return createEnvelopeResponse.getData();
    }
    //ds-snippet-end:eSign29Step4

    public static BrandsResponse getBrands(
            AccountsApi accountsApi,
            String accountId
    ) throws ApiException {
        var listBrandsResponse = accountsApi.listBrandsWithHttpInfo(accountId, accountsApi.new ListBrandsOptions());
        Map<String, List<String>> headers = listBrandsResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return listBrandsResponse.getData();
    }

    // Creates an envelope. The envelope has one recipient who should sign an
    // attached document. Attached document is read from a local directory.
    // Also the envelope contains a brand Id which is created on EG028 example.
    //ds-snippet-start:eSign29Step3
    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String brandId
    ) throws IOException {
        // Reads a file from a local directory and create Document object.
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        // Create a signer recipient to sign the document and associate sign tab
        Tabs tabs = EnvelopeHelpers.createSingleSignerTab("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X);
        Signer signer = new Signer()
                .email(signerEmail)
                .name(signerName)
                .recipientId("1")
                .routingOrder("1")
                .tabs(tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        // Return envelope definition using created objects: the document,
        // the recipients and obtained brand Id.
        return new EnvelopeDefinition()
                .emailSubject("EG029. Please Sign")
                .documents(List.of(document))
                .recipients(recipients)
                .brandId(brandId)
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
    }
    //ds-snippet-end:eSign29Step3
}
