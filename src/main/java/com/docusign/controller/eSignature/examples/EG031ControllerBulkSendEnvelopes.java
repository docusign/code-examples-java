package com.docusign.controller.eSignature.examples;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docusign.DSConfiguration;
import com.docusign.esign.api.BulkEnvelopesApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BulkSendBatchStatus;
import com.docusign.esign.model.BulkSendRequest;
import com.docusign.esign.model.BulkSendingCopy;
import com.docusign.esign.model.BulkSendingCopyRecipient;
import com.docusign.esign.model.BulkSendingList;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.CustomFields;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.TextCustomField;

/**
 * This code example demonstrates how to send envelopes in bulk to multiple
 * recipients with the eSignature API. To accomplish the task, you will first
 * create a bulk send recipients list, and then create an envelope. From there,
 * you will combine the envelope and bulk list to initiate bulk send
 */
@Controller
@RequestMapping("/eg031")
public class EG031ControllerBulkSendEnvelopes extends AbstractEsignatureController {

    private static final long BULK_REQUEST_DELAY = 15L;
    private static final String BULK_SIGNER_EMAIL_PLACEHOLDER = "MultiBulkRecipients-%s@docusign.com";
    private static final String BULK_SIGNER_NAME_PLACEHOLDER = "Multi Bulk Recipients::%s";
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "Lorem Ipsum";
    private static final int ANCHOR_OFFSET_Y = -5;
    private static final int ANCHOR_OFFSET_X = 15;

    private final Session session;
    private final User user;


    @Autowired
    public EG031ControllerBulkSendEnvelopes(DSConfiguration config, Session session, User user) {
        super(config, "eg031", "Bulk send envelopes");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {

        // Construct your API headers
        // Step 2 start
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        // Step 2 end

        // Submit a bulk list
        // Step 3-1 start
        String accountId = session.getAccountId();
        BulkEnvelopesApi bulkEnvelopesApi = new BulkEnvelopesApi(apiClient);
        BulkSendingList sendingList = getSendingList(args);
        String bulkListId = bulkEnvelopesApi.createBulkSendList(accountId, sendingList).getListId();
        // Step 3-1 end

        // Create an envelope
        // Step 4-1 start
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        String envelopeId = envelopesApi.createEnvelope(accountId, makeEnvelope()).getEnvelopeId();
        // Step 4-1 end

        // Attach your bulk list ID to the envelope
        // Step 5 start
        CustomFields customFields = createCustomFields(bulkListId);
        envelopesApi.createCustomFields(accountId, envelopeId, customFields);
        // Step 5 end

        // Add placeholder recipients
        // Step 6 start
        envelopesApi.createRecipient(accountId, envelopeId, envelopesApi.listRecipients(accountId, envelopeId));
        // Step 6 end

        // Initiate bulk send
        // Step 7 start
        BulkSendRequest request = new BulkSendRequest();
        request.setEnvelopeOrTemplateId(envelopeId);
        String batchId = bulkEnvelopesApi.createBulkSendRequest(accountId, bulkListId, request).getBatchId();
        // Step 7 end

        // Confirm successful bulk send
        // Step 8 start
        try {
            TimeUnit.SECONDS.sleep(BULK_REQUEST_DELAY);
            // For 2000 recipients, it can take about an hour
            BulkSendBatchStatus status = bulkEnvelopesApi.getBulkSendBatchStatus(accountId, batchId);
            // Step 8 end
            DoneExample.createDefault(title)
                    .withJsonObject(status)
                    .withMessage(String.join("", "Results from BulkSend:getBulkSendBatchStatus method:"))
                    .addToModel(model);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return null;
        }

        return DONE_EXAMPLE_PAGE;
    }

    // Step 3-2 start
    private static BulkSendingList getSendingList(WorkArguments args) {
        List<BulkSendingCopy> copies = List.of(
                createBulkSending(args.getSignerName(), args.getSignerEmail(), args.getCcName(), args.getCcEmail()),
                createBulkSending(args.getSignerName2(), args.getSignerEmail2(), args.getCcName2(), args.getCcEmail2())
                );
        return new BulkSendingList()
                .name("sample.csv")
                .bulkCopies(copies);
    }
    // Step 3-2 end

    private static BulkSendingCopy createBulkSending(String signerName,
            String signerEmail, String ccName, String ccEmail) {
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

    // Step 4-2 start
    private static EnvelopeDefinition makeEnvelope() throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");
        return new EnvelopeDefinition()
                .documents(List.of(document))
                .envelopeIdStamping(DsModelUtils.TRUE)
                .emailSubject("EG031 Please sign")
                .status(EnvelopeHelpers.ENVELOPE_STATUS_CREATED)
                .recipients(createRecipients());
    }
    // Step 4-2 end

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
        Signer signer = createSignerPlaceholder(EnvelopeHelpers.SIGNER_ROLE_NAME, "1", "1");
        CarbonCopy cc = createCCPlaceholder(EnvelopeHelpers.CC_ROLE_NAME, "2", "2");
        return new Recipients()
                .signers(List.of(signer))
                .carbonCopies(List.of(cc));
    }

    private static Signer createSignerPlaceholder(String roleName, String recipientId, String routingOrder) {
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

    private static CarbonCopy createCCPlaceholder(String roleName, String recipientId, String routingOrder) {
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
