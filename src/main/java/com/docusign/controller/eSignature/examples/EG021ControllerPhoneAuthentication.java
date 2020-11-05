package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientPhoneAuthentication;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * Send an envelope with a recipient using Phone Authentication.
 */
@Controller
@RequestMapping("/eg021")
public class EG021ControllerPhoneAuthentication extends AbstractEsignatureController {

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "Lorem";

    private final Session session;
    private final User user;


    @Autowired
    public EG021ControllerPhoneAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg021", "Signing request by email");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Step 1: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 2: Construct your envelope JSON body
        EnvelopeDefinition envelope = createEnvelope(args.getSignerName(), args.getSignerEmail(), args.getPhoneNumber());

        // Step 3: Call the eSignature REST API
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title)
                .withJsonObject(results)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                        + results.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    private static EnvelopeDefinition createEnvelope(String signerName, String signerEmail, String phoneNumber) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");

        SignHere signHere = new SignHere();
        signHere.setName("SignHereTab");
        signHere.setXPosition("75");
        signHere.setYPosition("572");
        signHere.setTabLabel("SignHereTab");
        signHere.setPageNumber("1");
        signHere.setDocumentId(doc.getDocumentId());
        // A 1- to 8-digit integer or 32-character GUID to match recipient IDs on your own systems.
        // This value is referenced in the Tabs element below to assign tabs on a per-recipient basis.
        signHere.setRecipientId("1");
        List<String> RECIPIENT_PHONE_NUMBERS = List.of(phoneNumber);
        RecipientPhoneAuthentication phoneAuth = new RecipientPhoneAuthentication();
        phoneAuth.setRecipMayProvideNumber("true");
        phoneAuth.setSenderProvidedNumbers(RECIPIENT_PHONE_NUMBERS);

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setEmail(signerEmail);
        signer.setRoutingOrder("1");
        signer.setStatus(EnvelopeHelpers.SIGNER_STATUS_CREATED);
        signer.setDeliveryMethod(EnvelopeHelpers.DELIVERY_METHOD_EMAIL);
        signer.setRecipientId(signHere.getRecipientId());
        signer.setTabs(EnvelopeHelpers.createSignerTabs(signHere));
        signer.setRequireIdLookup("true");
        signer.setPhoneAuthentication(phoneAuth);
        signer.setIdCheckConfigurationName("Phone Auth $");

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please Sign");
        envelope.setEnvelopeIdStamping("true");
        envelope.setEmailBlurb("Sample text for email body");
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);
        envelope.setDocuments(Arrays.asList(doc));

        return envelope;
    }
    // ***DS.snippet.0.end
}
