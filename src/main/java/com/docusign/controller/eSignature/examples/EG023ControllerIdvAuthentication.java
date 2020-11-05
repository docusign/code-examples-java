package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountIdentityVerificationResponse;
import com.docusign.esign.model.AccountIdentityVerificationWorkflow;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientIdentityVerification;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * ID Verification authentication
 */
@Controller
@RequestMapping("/eg023")
public class EG023ControllerIdvAuthentication extends AbstractEsignatureController {

    private static final Logger logger = LoggerFactory.getLogger(EG023ControllerIdvAuthentication.class);
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "Lorem";
    private final Session session;
    private final User user;

    @Autowired
    public EG023ControllerIdvAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg023", "ID Verification Authentication");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException, IOException {
        // Step 1: Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        //Step 2: Retrieve the workflow ID
        AccountsApi workflowDetails = new AccountsApi(apiClient);
        AccountIdentityVerificationResponse workflowRes = workflowDetails.getAccountIdentityVerification(session.getAccountId());
        List<AccountIdentityVerificationWorkflow> identityVerification = workflowRes.getIdentityVerification();
        if (identityVerification == null || identityVerification.isEmpty()) {
            throw new ApiException("Error. Cant get AccountIdentityVerificationWorkflow");
        }
        String workflowId = identityVerification.get(0).getWorkflowId();
        logger.info("workflowId = " + workflowId);
        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = createEnvelope(args.getSignerName(), args.getSignerEmail(), workflowId);
        // Step 4: Create envelope
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title)
                .withJsonObject(results)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                        + results.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    private static EnvelopeDefinition createEnvelope(String signerName, String signerEmail,
                                                     String workflowId) {
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign");
        envelopeDefinition.setEmailBlurb("Sample text for email body");
        envelopeDefinition.setStatus("Sent");


        byte[] fileBytes = new byte[0];
        try {
            fileBytes = EnvelopeHelpers.readFile(DOCUMENT_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc1 = new Document();
        doc1.setDocumentBase64(Base64.getEncoder().encodeToString(fileBytes));
        doc1.setDocumentId("1");
        doc1.setFileExtension("pdf");
        doc1.setName("Lorem");

        SignHere signHere1 = new SignHere();
        signHere1.setName("SignHereTab");
        signHere1.setXPosition("75");
        signHere1.setYPosition("572");
        signHere1.setTabLabel("SignHereTab");
        signHere1.setPageNumber("1");
        signHere1.setDocumentId("1");
        // A 1- to 8-digit integer or 32-character GUID to match recipient IDs on your own systems.
        // This value is referenced in the Tabs element below to assign tabs on a per-recipient basis.
        signHere1.setRecipientId("1"); //represents your {RECIPIENT_ID}

        Tabs signer1Tabs = new Tabs();
        signer1Tabs.setSignHereTabs(Collections.singletonList(signHere1));
        RecipientIdentityVerification workflow = new RecipientIdentityVerification();
        workflow.setWorkflowId(workflowId);

        Signer signer1 = new Signer();
        signer1.setName(signerName);
        signer1.setEmail(signerEmail);
        signer1.setRoleName("");
        signer1.setNote("");
        signer1.setStatus("created");
        signer1.setDeliveryMethod("email");
        signer1.setRecipientId("1"); //represents your {RECIPIENT_ID}
        signer1.setIdentityVerification(workflow);
        signer1.setTabs(signer1Tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer1));

        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setDocuments(Collections.singletonList(doc1));
        return envelopeDefinition;
    }
}
