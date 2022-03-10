package com.docusign.controller.eSignature.examples;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeDelayRuleApiModel;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.ScheduledSendingApiModel;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.Workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Send an envelope with a remote (email) signer and cc recipient.<br />
 * The envelope includes a pdf, Word, and HTML document. Anchor text is used to
 * position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg035")
public class EG035ControllerScheduledSending extends AbstractEsignatureController {


    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;


    private final Session session;
    private final User user;

    @Autowired
    public EG035ControllerScheduledSending(DSConfiguration config, Session session, User user) {
        super(config, "eg035", "Schedule an Envelope");
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


        System.out.println("RESUMEDATE");
        System.out.println(args.getResumeDate()+"T00:00:00Z");

        EnvelopeDefinition envelope = makeEnvelope(args);


        // Step 3 start
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);
        // Step 3 end

        System.out.println("ENVELOPE");
        System.out.println(results.getEnvelopeId());

        // process results
        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                        + results.getEnvelopeId() + ".")
                .withJsonObject(results)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

        // document 1 (PDF) has sign here anchor tag /sn1/
        //
        // The envelope has one recipient.
        // recipient 1 - signer
        
        // create the envelope definition

    public static EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {
        // The DocuSign platform searches throughout your envelope's documents
        // for matching anchor strings.

        // Step 2 start

        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
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


        // The order in the docs array determines the order in the envelope
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document");
        envelope.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "1")));
        envelope.setRecipients(EnvelopeHelpers.createSingleRecipient(signer));

        // Add the workflow to schedule the envelope with a delay
        EnvelopeDelayRuleApiModel delayRule = new EnvelopeDelayRuleApiModel();

        delayRule.setResumeDate(args.getResumeDate()+"T00:00:00Z");
        
        ScheduledSendingApiModel scheduledSendingApiModel = new ScheduledSendingApiModel();
        scheduledSendingApiModel.setRules(Arrays.asList(delayRule));

        Workflow workflow = new Workflow();
        workflow.setScheduledSending(scheduledSendingApiModel);
        envelope.setWorkflow(workflow);

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelope.setStatus(args.getStatus());

        return envelope;

        // Step 2 end
    }
}
