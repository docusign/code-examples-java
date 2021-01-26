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
    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";

    private final Session session;
    private final User user;


    @Autowired
    public EG031ControllerBulkSendEnvelopes(DSConfiguration config, Session session, User user) {
        super(config, "eg031", "Bulk sending envelopes to multiple recipients");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {

        // Step 2. Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        String accountId = session.getAccountId();

        // Step 3. Submit a bulk list
        BulkEnvelopesApi bulkEnvelopesApi = new BulkEnvelopesApi(apiClient);
        BulkSendingList sendingList = getSendingList(args);
        String bulkListId = bulkEnvelopesApi.createBulkSendList(accountId, sendingList).getListId();

        // Step 4. Create an envelope
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        String envelopeId = envelopesApi.createEnvelope(accountId, makeEnvelope()).getEnvelopeId();

        // Step 5. Attach your bulk list ID to the envelope
        CustomFields customFields = createCustomFields(bulkListId);
        envelopesApi.createCustomFields(accountId, envelopeId, customFields);

        // Step 6. Add placeholder recipients
        envelopesApi.createRecipient(accountId, envelopeId, createRecipients());

        // Step 7. Initiate bulk send
        BulkSendRequest request = new BulkSendRequest();
        request.setEnvelopeOrTemplateId(envelopeId);
        String batchId = bulkEnvelopesApi.createBulkSendRequest(accountId, bulkListId, request).getBatchId();

        // Step 8. Confirm successful bulk send 
        try {
            TimeUnit.SECONDS.sleep(BULK_REQUEST_DELAY);
            // For 2000 recipients, it can take about an hour
            BulkSendBatchStatus status = bulkEnvelopesApi.getBulkSendBatchStatus(accountId, batchId);
            DoneExample.createDefault(title)
                    .withJsonObject(status)
                    .withMessage(String.join("", "Bulk request queued to ", status.getQueued(), " user lists."))
                    .addToModel(model);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return null;
        }

        return DONE_EXAMPLE_PAGE;
    }

    private static BulkSendingList getSendingList(WorkArguments args) {
        List<BulkSendingCopy> copies = List.of(
                createBulkSending(args.getSignerName(), args.getSignerEmail(), args.getCcName(), args.getCcEmail()),
                createBulkSending(args.getSignerName2(), args.getSignerEmail2(), args.getCcName2(), args.getCcEmail2())
                );
        return new BulkSendingList()
                .name("sample.csv")
                .bulkCopies(copies);
    }

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

    private static EnvelopeDefinition makeEnvelope() throws IOException {
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
