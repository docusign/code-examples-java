package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.DsModelUtils;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.BulkEnvelopesApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class BulkSendEnvelopesService {
    private static final String BULK_SIGNER_EMAIL_PLACEHOLDER = "MultiBulkRecipients-%s@docusign.com";

    private static final String BULK_SIGNER_NAME_PLACEHOLDER = "Multi Bulk Recipients::%s";

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem Ipsum";

    private static final int ANCHOR_OFFSET_Y = -5;

    private static final int ANCHOR_OFFSET_X = 15;

    //ds-snippet-start:eSign31Step7
    public static BulkSendBatchStatus getBulkSendBatchStatus(
            BulkEnvelopesApi bulkEnvelopesApi,
            String accountId,
            String batchId,
            long bulkRequestDelay
    ) throws ApiException, InterruptedException {
        TimeUnit.SECONDS.sleep(bulkRequestDelay);
        // For 2000 recipients, it can take about an hour
        return bulkEnvelopesApi.getBulkSendBatchStatus(accountId, batchId);
    }
    //ds-snippet-end:eSign31Step7

    public static String bulkSendEnvelopes(
            BulkEnvelopesApi bulkEnvelopesApi,
            ApiClient apiClient,
            String signerName,
            String signerEmail,
            String ccName,
            String ccEmail,
            String signerName2,
            String signerEmail2,
            String ccName2,
            String ccEmail2,
            String accountId
    ) throws ApiException, IOException {
        BulkSendingList sendingList = BulkSendEnvelopesService.getSendingList(
                signerName,
                signerEmail,
                ccName,
                ccEmail,
                signerName2,
                signerEmail2,
                ccName2,
                ccEmail2
        );

        String bulkListId = bulkEnvelopesApi.createBulkSendList(accountId, sendingList).getListId();

        // Create an envelope
        //ds-snippet-start:eSign31Step4
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        String envelopeId = envelopesApi.createEnvelope(accountId, makeEnvelope()).getEnvelopeId();
        //ds-snippet-end:eSign31Step4

        // Attach your bulk list ID to the envelope
        //ds-snippet-start:eSign31Step5
        CustomFields customFields = createCustomFields(bulkListId);
        envelopesApi.createCustomFields(accountId, envelopeId, customFields);
        //ds-snippet-end:eSign31Step5

        // Initiate bulk send
        //ds-snippet-start:eSign31Step6
        BulkSendRequest request = new BulkSendRequest();
        request.setEnvelopeOrTemplateId(envelopeId);
        return bulkEnvelopesApi.createBulkSendRequest(accountId, bulkListId, request).getBatchId();
        //ds-snippet-end:eSign31Step6
    }

//ds-snippet-start:eSign31Step3
    public static BulkSendingList getSendingList(
            String signerName,
            String signerEmail,
            String ccName,
            String ccEmail,
            String signerName2,
            String signerEmail2,
            String ccName2,
            String ccEmail2
    ) {
        List<BulkSendingCopy> copies = List.of(
                createBulkSending(signerName, signerEmail, ccName, ccEmail),
                createBulkSending(signerName2, signerEmail2, ccName2, ccEmail2)
        );
        return new BulkSendingList()
                .name("sample.csv")
                .bulkCopies(copies);
    }

    public static BulkSendingCopy createBulkSending(
            String signerName,
            String signerEmail,
            String ccName,
            String ccEmail
    ) {
        BulkSendingCopyRecipient recipient1 = new BulkSendingCopyRecipient()
                .name(signerName)
                .email(signerEmail)
                .tabs(Collections.emptyList())
                .roleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        BulkSendingCopyRecipient recipient2 = new BulkSendingCopyRecipient()
                .name(ccName)
                .email(ccEmail)
                .tabs(Collections.emptyList())
                .roleName(EnvelopeHelpers.CC_ROLE_NAME);
        return new BulkSendingCopy()
                .recipients(List.of(recipient1, recipient2))
                .customFields(Collections.emptyList());
    }
//ds-snippet-end:eSign31Step3

    public static EnvelopeDefinition makeEnvelope() throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");
        return new EnvelopeDefinition()
                .documents(List.of(document))
                .envelopeIdStamping(DsModelUtils.TRUE)
                .emailSubject("EG031 Please sign")
                .status(EnvelopeHelpers.ENVELOPE_STATUS_CREATED)
                .recipients(createRecipients());
    }

    public static CustomFields createCustomFields(String bulkListId) {
        TextCustomField textCustomField = new TextCustomField()
                .name("mailingListId")
                .required(DsModelUtils.FALSE)
                .show(DsModelUtils.FALSE)
                .value(bulkListId);

        return new CustomFields()
                .listCustomFields(Collections.emptyList())
                .textCustomFields(List.of(textCustomField));
    }

    public static Recipients createRecipients() {
        Signer signer = createSignerPlaceholder(EnvelopeHelpers.SIGNER_ROLE_NAME, "1", "1");
        CarbonCopy cc = createCCPlaceholder(EnvelopeHelpers.CC_ROLE_NAME, "2", "2");
        return new Recipients()
                .signers(List.of(signer))
                .carbonCopies(List.of(cc));
    }

    public static Signer createSignerPlaceholder(String roleName, String recipientId, String routingOrder) {
        return new Signer()
                .name(String.format(BULK_SIGNER_NAME_PLACEHOLDER, roleName))
                .email(String.format(BULK_SIGNER_EMAIL_PLACEHOLDER, roleName))
                .roleName(roleName)
                .note("")
                .routingOrder(routingOrder)
                .status(EnvelopeHelpers.SIGNER_STATUS_CREATED)
                .deliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL)
                .recipientId(recipientId)
                .recipientType(roleName)
                .tabs(EnvelopeHelpers.createSingleSignerTab("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));
    }

    public static CarbonCopy createCCPlaceholder(String roleName, String recipientId, String routingOrder) {
        return new CarbonCopy()
                .name(String.format(BULK_SIGNER_NAME_PLACEHOLDER, roleName))
                .email(String.format(BULK_SIGNER_EMAIL_PLACEHOLDER, roleName))
                .roleName(roleName)
                .note("")
                .routingOrder(routingOrder)
                .status(EnvelopeHelpers.SIGNER_STATUS_CREATED)
                .deliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL)
                .recipientId(recipientId)
                .recipientType(roleName);
    }
}
