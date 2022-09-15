package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.BulkEnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BulkSendBatchStatus;
import com.docusign.controller.eSignature.services.BulkSendEnvelopesService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final Session session;
    private final User user;

    @Autowired
    public EG031ControllerBulkSendEnvelopes(DSConfiguration config, Session session, User user) {
        super(config, "eg031");
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

        // submit a bulk list
        // Step 3-1 start
        String accountId = session.getAccountId();
        BulkEnvelopesApi bulkEnvelopesApi = new BulkEnvelopesApi(apiClient);
        String batchId = BulkSendEnvelopesService.bulkSendEnvelopes(
                bulkEnvelopesApi,
                apiClient,
                args.getSignerName(),
                args.getSignerEmail(),
                args.getCcName(),
                args.getCcEmail(),
                args.getSignerName2(),
                args.getSignerEmail2(),
                args.getCcName2(),
                args.getCcEmail2(),
                session.getAccountId()
        );

        // Confirm successful bulk send
        // Step 7 start
        try {
            TimeUnit.SECONDS.sleep(BULK_REQUEST_DELAY);
            // For 2000 recipients, it can take about an hour
            BulkSendBatchStatus status = bulkEnvelopesApi.getBulkSendBatchStatus(accountId, batchId);
            // Step 7 end
            DoneExample.createDefault(this.codeExampleText.ExampleName)
                    .withJsonObject(status)
                    .withMessage(String.join(
                            "",
                            "Bulk request queued to ", status.getQueued(), " user lists."))
                    .withMessage(this.codeExampleText.ResultsPageText)
                    .addToModel(model);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return null;
        }
        return DONE_EXAMPLE_PAGE;
    }
}
