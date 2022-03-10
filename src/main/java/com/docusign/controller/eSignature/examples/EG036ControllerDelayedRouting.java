package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.DelayedRoutingApiModel;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeDelayRuleApiModel;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.Workflow;
import com.docusign.esign.model.WorkflowStep;

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
@RequestMapping("/eg036")
public class EG036ControllerDelayedRouting extends AbstractEsignatureController {

    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";
    private static final String DOCX_DOCUMENT_FILE_NAME = "World_Wide_Corp_Battle_Plan_Trafalgar.docx";
    private static final String DOCX_DOCUMENT_NAME = "Battle Plan";
    private static final int ANCHOR_OFFSET_Y = 10;
    private static final int ANCHOR_OFFSET_X = 20;
    
    private static final int ANCHOR_OFFSET_X_2 = 120;

    private final Session session;
    private final User user;

    @Autowired
    public EG036ControllerDelayedRouting(DSConfiguration config, Session session, User user) {
        super(config, "eg036", "Send an envelope with delayed routing");
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

        // Step 3 start
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);
        // Step 3 end


        // process results
        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                        + results.getEnvelopeId() + ".")
                .withJsonObject(results)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    // document 1 (docx) has tag /sn1/
    // document 2 (pdf) has tag /sn1/
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

        // Step 2 start

        Tabs signer1Tabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X));
       
        Tabs signer2Tabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/sn1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X_2));
        
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
        signer.setTabs(signer1Tabs);

        Signer signer2 = new Signer();
        signer2.setEmail(args.getSignerEmail2());
        signer2.setName(args.getSignerName2());
        signer2.setRecipientId("2");
        signer2.setRoutingOrder("2");
        signer2.setTabs(signer2Tabs);
     
        // The order in the docs array determines the order in the envelope
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocumentFromFile(DOCX_DOCUMENT_FILE_NAME, DOCX_DOCUMENT_NAME, "2"),
                EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "3")));
        envelope.setRecipients(EnvelopeHelpers.createTwoSigners(signer, signer2));

        // Add the workflow to delay the second recipient

        String delay = args.getDelay() + ":00:00";
        EnvelopeDelayRuleApiModel rule = new EnvelopeDelayRuleApiModel();
        rule.setDelay(delay);

        DelayedRoutingApiModel delayedRouting = new DelayedRoutingApiModel();
        delayedRouting.setRules(Arrays.asList(rule));

        WorkflowStep workflowStep = new WorkflowStep();
        workflowStep.setAction("pause_before");
        workflowStep.setTriggerOnItem("routing_order");
        workflowStep.setItemId("2");
        workflowStep.setStatus("pending");
        workflowStep.setDelayedRouting(delayedRouting);

        Workflow workflow = new Workflow();
        workflow.setWorkflowSteps(Arrays.asList(workflowStep));
        envelope.setWorkflow(workflow);

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelope.setStatus(args.getStatus());

        return envelope;

        // Step 2 end
    }
}
