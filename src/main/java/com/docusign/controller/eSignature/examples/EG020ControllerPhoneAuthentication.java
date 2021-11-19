package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountIdentityVerificationResponse;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientIdentityInputOption;
import com.docusign.esign.model.RecipientIdentityPhoneNumber;
import com.docusign.esign.model.RecipientIdentityVerification;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

/**
 * Example 020: Phone Authentication for recipient
 */
@Controller
@RequestMapping("/eg020")
public class EG020ControllerPhoneAuthentication extends AbstractEsignatureController {
    // For List.of you could even do groups of numbers such as
    // List.of("415-555-1212", "415-555-3434");
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
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        // Step 2 end

        // Step 4a start
        String workFlowId = getWorkflowId(accountId);
        EnvelopeDefinition envelope = createEnvelope(args.getSignerName(), args.getSignerEmail(), args.getCountryCode(),
                args.getPhoneNumber(), workFlowId);
        // Step 4a end

        // Step 5 start
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
        // Step 5 end

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title).withJsonObject(results)
                .withMessage(
                        "The envelope has been created and sent!<br />Envelope ID " + results.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    // Step 4b start
    private static EnvelopeDefinition createEnvelope(String signerName, String signerEmail, String countryCode,
            String phone, String workFlowId) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        SignHere signHere = new SignHere();
        signHere.setName("SignHereTab");
        signHere.setXPosition("75");
        signHere.setYPosition("572");
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

    // retreives the workflow ID
    private String getWorkflowId(String accountId) {
        try {
            AccountsApi workflowDetails = createAccountsApi(session.getBasePath(), user.getAccessToken());
            AccountIdentityVerificationResponse workflowResponse = workflowDetails
                    .getAccountIdentityVerification(accountId);

            // Check that idv authentication is enabled
            // The workflow ID is a hard-coded value which is unique to this phone
            // authentication workflow
            if (workflowResponse.getIdentityVerification() != null) {
                return "c368e411-1592-4001-a3df-dca94ac539ae";
            } else {
                throw new RuntimeException("Identity Verification is not enabled for this account");
            }

        } catch (Exception e) {
            return null;
        }

    }
    // Step 4b end
}
