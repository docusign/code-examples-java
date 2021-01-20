package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.common.DocumentType;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.RecipientAdditionalNotification;
import com.docusign.esign.model.RecipientPhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;


/**
 * Send an envelope with a remote (email) signer and cc recipient.<br />
 * The envelope includes a pdf, Word, and HTML document. Anchor text is used to
 * position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg035")
public class EG035ControllerSMSDelivery extends AbstractEsignatureController {

    private static final String HTML_DOCUMENT_FILE_NAME = "templates/candy-bonbon.ftl";
    private static final String HTML_DOCUMENT_NAME = "Order acknowledgement";
    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";
    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;

    private final Session session;
    private final User user;

    @Autowired
    public EG035ControllerSMSDelivery(DSConfiguration config, Session session, User user) {
        super(config, "eg035", "Signing request by email");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Set status for the makeEnvelope method
        if (!EnvelopeHelpers.ENVELOPE_STATUS_CREATED.equalsIgnoreCase(args.getStatus())) {
            args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        }

        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        EnvelopeDefinition envelope = makeEnvelope(args);
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        // process results
        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                        + results.getEnvelopeId() + ".")
                .withJsonObject(results)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    // document 1 (html) has tag **signature_1**
    // document 2 (docx) has tag /sn1/
    // document 3 (pdf) has tag /sn1/
    //
    // The envelope has two recipients.
    // recipient 1 - signer
    // recipient 2 - cc
    // The envelope will be sent first to the signer. After it is signed,
    // a copy is sent to the cc person.
    public static EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {
        // The DocuSign platform searches throughout your envelope's documents
        // for matching anchor strings. So the signHere2 tab will be used in
        // both document 2 and 3 since they use the same anchor string for
        // their "signer 1" tabs.
        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("**signature_1**", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X),
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));

        // Create a signer recipient to sign the document, identified by name
        // and email. We're setting the parameters via the object creation.
        // RoutingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.
        Signer signer = new Signer();
        signer.setEmail(args.getSignerEmail());
        signer.setName(args.getSignerName());
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signerTabs);

        RecipientAdditionalNotification smsDelivery = new RecipientAdditionalNotification();
        smsDelivery.setSecondaryDeliveryMethod("SMS");

        RecipientPhoneNumber phoneNumber = new RecipientPhoneNumber();
        phoneNumber.setCountryCode(args.getCountryCode());
        phoneNumber.setNumber(args.getPhoneNumber());
        smsDelivery.phoneNumber(phoneNumber);
        signer.setAdditionalNotifications(Arrays.asList(smsDelivery));
        
        // create a cc recipient to receive a copy of the documents, identified by name and email
        CarbonCopy cc = new CarbonCopy();
        cc.setEmail(args.getCcEmail());
        cc.setName(args.getCcName());
        cc.setRecipientId("2");
        cc.setRoutingOrder("2");
        
        RecipientAdditionalNotification ccSmsDelivery = new RecipientAdditionalNotification();
        ccSmsDelivery.setSecondaryDeliveryMethod("SMS");
        
        RecipientPhoneNumber ccPhoneNumber = new RecipientPhoneNumber();
        ccPhoneNumber.setCountryCode(args.getCcCountryCode());
        ccPhoneNumber.setNumber(args.getCcPhoneNumber());
        ccSmsDelivery.phoneNumber(ccPhoneNumber);
        cc.setAdditionalNotifications(Arrays.asList(ccSmsDelivery));

        // The order in the docs array determines the order in the envelope
        byte[] htmlDocument = EnvelopeHelpers.createHtmlFromTemplateFile(HTML_DOCUMENT_FILE_NAME, "args", args);
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocument(htmlDocument, HTML_DOCUMENT_NAME,
                        DocumentType.HTML.getDefaultFileExtention(), "1"),
                EnvelopeHelpers.createDocumentFromFile(DOCX_DOCUMENT_FILE_NAME, DOCX_DOCUMENT_NAME, "2"),
                EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "3")));
        envelope.setRecipients(EnvelopeHelpers.createRecipients(signer, cc));
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelope.setStatus(args.getStatus());

        return envelope;
    }
}
