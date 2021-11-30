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
import com.docusign.esign.model.RecipientIdentityInputOption;
import com.docusign.esign.model.RecipientIdentityPhoneNumber;
import com.docusign.esign.model.RecipientIdentityVerification;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * Example 020: Phone Authentication for recipient
 */
@Controller
@RequestMapping("/eg020")
public class EG020ControllerPhoneAuthentication extends AbstractEsignatureController {
    
    private static final Logger logger = LoggerFactory.getLogger(EG023ControllerIdvAuthentication.class);
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "Lorem";

    private final Session session;
    private final User user;

    @Autowired
    public EG020ControllerPhoneAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg020", "Require Phone Authentication for a Recipient");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        String accountId = session.getAccountId();

        // Step 2 start
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        // Step 2 end
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 3 start
        AccountsApi workflowDetails = new AccountsApi(apiClient);
        AccountIdentityVerificationResponse workflowRes = workflowDetails.getAccountIdentityVerification(session.getAccountId());
        List<AccountIdentityVerificationWorkflow> identityVerification = workflowRes.getIdentityVerification();
        String workflowId = "";
        for (int i = 0; i < identityVerification.size(); i++)
        {
            if (identityVerification.get(i).getDefaultName().equals("Phone Authentication"))
            {
                workflowId = identityVerification.get(i).getWorkflowId();
            }
        }
        logger.info("workflowId = " + workflowId);
        // Step 3 end
        EnvelopeDefinition envelope = createEnvelope(args.getSignerName(), args.getSignerEmail(), args.getCountryCode(),
                args.getPhoneNumber(), workflowId);
        // Step 4.1 start
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
        // Step 4.1 end

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title).withJsonObject(results)
                .withMessage(
                        "The envelope has been created and sent!<br />Envelope ID " + results.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    // Step 4.2 start
    private static EnvelopeDefinition createEnvelope(String signerName, String signerEmail, String countryCode,
            String phone, String workFlowId) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        SignHere signHere = new SignHere();
        signHere.setName("SignHereTab");
        signHere.setXPosition("200");
        signHere.setYPosition("160");
        signHere.setTabLabel("SignHereTab");
        signHere.setPageNumber("1");
        signHere.setDocumentId(doc.getDocumentId());
        // A 1- to 8-digit integer or 32-character GUID to match recipient IDs on your
        // own systems.
        // This value is referenced in the Tabs element below to assign tabs on a
        // per-recipient basis.
        signHere.setRecipientId("1");

        RecipientIdentityPhoneNumber phoneNumber = new RecipientIdentityPhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setNumber(phone);

        RecipientIdentityInputOption inputOption = new RecipientIdentityInputOption();
        inputOption.setName("phone_number_list");
        inputOption.setValueType("PhoneNumberList");
        inputOption.setPhoneNumberList(Arrays.asList(phoneNumber));

        RecipientIdentityVerification identityVerifcation = new RecipientIdentityVerification();

        identityVerifcation.setWorkflowId(workFlowId);
        identityVerifcation.setInputOptions(Arrays.asList(inputOption));

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setEmail(signerEmail);
        signer.setRoutingOrder("1");
        signer.setStatus(EnvelopeHelpers.SIGNER_STATUS_CREATED);
        signer.setDeliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL);
        signer.setRecipientId(signHere.getRecipientId());
        signer.setTabs(EnvelopeHelpers.createSignerTabs(signHere));
        signer.setIdentityVerification(identityVerifcation);

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please Sign");
        envelope.setDocuments(Arrays.asList(doc));
        envelope.setEnvelopeIdStamping("true");
        envelope.setEmailBlurb("Sample text for email body");
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);

        return envelope;
    }
    // Step 4.2 end

}
