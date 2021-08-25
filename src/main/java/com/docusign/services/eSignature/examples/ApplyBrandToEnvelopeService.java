package com.docusign.services.eSignature.examples;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class ApplyBrandToEnvelopeService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "EG025 Lorem Ipsum";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;

    public static EnvelopeSummary applyBrandToEnvelope(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        return envelopesApi.createEnvelope(accountId, envelope);
    }

    // Creates an envelope. The envelope has one recipient who should sign an
    // attached document. Attached document is read from a local directory.
    // Also the envelope contains a brand Id which is created on EG028 example.
    public static EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {
        // Reads a file from a local directory and create Document object.
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        // Create a signer recipient to sign the document and associate sign tab
        Tabs tabs = EnvelopeHelpers.createSingleSignerTab("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X);
        Signer signer = new Signer()
                .email(args.getSignerEmail())
                .name(args.getSignerName())
                .recipientId("1")
                .routingOrder("1")
                .tabs(tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));

        // Return envelope definition using created objects: the document,
        // the recipients and obtained brand Id.
        return new EnvelopeDefinition()
                .emailSubject("EG029. Please Sign")
                .documents(List.of(document))
                .recipients(recipients)
                .brandId(args.getBrandId())
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
    }
}
