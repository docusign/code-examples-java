package com.docusign.services.eSignature.examples;

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

public final class BulkSendEnvelopesService {
    private static final String BULK_SIGNER_EMAIL_PLACEHOLDER = "MultiBulkRecipients-%s@docusign.com";
    private static final String BULK_SIGNER_NAME_PLACEHOLDER = "Multi Bulk Recipients::%s";
    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";

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
        // Step 3. Submit a bulk list
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

        // Step 4. Create an envelope
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        String envelopeId = envelopesApi.createEnvelope(
                accountId,
                BulkSendEnvelopesService.makeEnvelope()).getEnvelopeId();

        // Step 5. Attach your bulk list ID to the envelope
        CustomFields customFields = BulkSendEnvelopesService.createCustomFields(bulkListId);
        envelopesApi.createCustomFields(accountId, envelopeId, customFields);

        // Step 6. Add placeholder recipients
        envelopesApi.createRecipient(accountId, envelopeId, BulkSendEnvelopesService.createRecipients());

        // Step 7. Initiate bulk send
        BulkSendRequest request = new BulkSendRequest();
        request.setEnvelopeOrTemplateId(envelopeId);
        return bulkEnvelopesApi.createBulkSendRequest(accountId, bulkListId, request).getBatchId();
    }

    private static BulkSendingList getSendingList(
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

    private static BulkSendingCopy createBulkSending(
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

    public static EnvelopeDefinition makeEnvelope() throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCX_DOCUMENT_FILE_NAME, DOCX_DOCUMENT_NAME, "1");
        return new EnvelopeDefinition()
                .documents(List.of(document))
                .envelopeIdStamping(DsModelUtils.TRUE)
                .emailSubject("EG031 Please sign")
                .status(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);
    }

    private static CustomFields createCustomFields(String bulkListId) {
        TextCustomField textCustomField = new TextCustomField()
                .name("mailingListId")
                .required(DsModelUtils.FALSE)
                .show(DsModelUtils.FALSE)
                .value(bulkListId);

        return new CustomFields()
                .listCustomFields(Collections.emptyList())
                .textCustomFields(List.of(textCustomField));
    }

    private static Recipients createRecipients() {
        List<Signer> signers = List.of(
                createSignerPlaceholder(EnvelopeHelpers.SIGNER_ROLE_NAME, "1"),
                createSignerPlaceholder(EnvelopeHelpers.CC_ROLE_NAME, "2"));
        return new Recipients()
                .signers(signers);
    }

    private static Signer createSignerPlaceholder(String roleName, String recipientId) {
        return new Signer()
                .name(String.format(BULK_SIGNER_NAME_PLACEHOLDER, roleName))
                .email(String.format(BULK_SIGNER_EMAIL_PLACEHOLDER, roleName))
                .roleName(roleName)
                .note("")
                .routingOrder("1")
                .status(EnvelopeHelpers.SIGNER_STATUS_CREATED)
                .deliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL)
                .recipientId(recipientId)
                .recipientType(EnvelopeHelpers.SIGNER_ROLE_NAME);
    }
}
