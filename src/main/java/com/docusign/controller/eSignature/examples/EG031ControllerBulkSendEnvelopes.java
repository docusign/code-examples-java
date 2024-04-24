package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.BulkSendEnvelopesService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.BulkEnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BulkSendBatchStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    public EG031ControllerBulkSendEnvelopes(DSConfiguration config, Session session, User user) {
        super(config, "eg031", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {

        // Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        // submit a bulk list
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
        try {

            BulkSendBatchStatus status = BulkSendEnvelopesService.getBulkSendBatchStatus(bulkEnvelopesApi, accountId, batchId, BULK_REQUEST_DELAY);

            DoneExample.createDefault(getTextForCodeExample().ExampleName)

                    .withJsonObject(status)
                    .withMessage(String.join(
                            "",
                            "Bulk request queued to ", status.getQueued(), " user lists."))
                    .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                    .addToModel(model, config);

            return DONE_EXAMPLE_PAGE;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            new DoneExample()
                    .withTitle(exampleName)
                    .withName(title)
                    .withMessage(
                            getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage
                                    + "< br />Reason:"
                                    + exception.getMessage()
                    )
                    .addToModel(model, config);

            return ERROR_PAGE;
        }
    }
}
