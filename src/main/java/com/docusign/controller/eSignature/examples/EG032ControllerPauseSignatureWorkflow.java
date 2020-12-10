package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * This topic demonstrates how to create an envelope where the workflow
 * is paused before the envelope is sent to a second recipient.
 */
@Controller
@RequestMapping("/eg032")
public class EG032ControllerPauseSignatureWorkflow extends AbstractEsignatureController {

    private static final String DOCUMENT_FILE_NAME = "Welcome.txt";
    private static final String DOCUMENT_ID = "1";
    private static final String DOCUMENT_NAME = "Welcome";

    private final Session session;
    private final User user;


    @Autowired
    public EG032ControllerPauseSignatureWorkflow(DSConfiguration config, Session session, User user) {
        super(config, "eg032", "Pause a signature workflow.");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException, IOException {

        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = createEnvelope(args);

        // Step 4: Call the eSignature REST API
        EnvelopeSummary results = envelopesApi.createEnvelope(this.session.getAccountId(), envelope);

        this.session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(this.title)
                .withJsonObject(results)
                .withMessage(
                        "The envelope where the workflow is paused before the envelope is sent to a second recipient " +
                        "has been created and sent!<br />Envelope ID "
                        + this.session.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    private static EnvelopeDefinition createEnvelope(WorkArguments args) throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, DOCUMENT_ID);

        WorkflowStep workflowStep = new WorkflowStep();
        workflowStep.setAction(EnvelopeHelpers.WORKFLOW_STEP_ACTION_PAUSE);
        workflowStep.setTriggerOnItem(EnvelopeHelpers.WORKFLOW_TRIGGER_ROUTING_ORDER);
        workflowStep.setItemId("2");

        Workflow workflow = new Workflow();
        workflow.setWorkflowSteps(Collections.singletonList(workflowStep));

        SignHere signHere1 = new SignHere();
        signHere1.setName("SignHereTabs");
        signHere1.setXPosition("200");
        signHere1.setYPosition("200");
        signHere1.setTabLabel("Sign Here");
        signHere1.setPageNumber("1");
        signHere1.setDocumentId(document.getDocumentId());

        Signer signer1 = new Signer();
        signer1.setName(args.getSignerName());
        signer1.setEmail(args.getSignerEmail());
        signer1.setRoutingOrder("1");
        signer1.setRecipientId("1");
        signer1.setTabs(EnvelopeHelpers.createSignerTabs(signHere1));

        SignHere signHere2 = new SignHere();
        signHere2.setName("SignHereTabs");
        signHere2.setXPosition("300");
        signHere2.setYPosition("200");
        signHere2.setTabLabel("Sign Here");
        signHere2.setPageNumber("1");
        signHere2.setDocumentId(document.getDocumentId());

        Signer signer2 = new Signer();
        signer2.setName(args.getSignerName2());
        signer2.setEmail(args.getSignerEmail2());
        signer2.setRoutingOrder("2");
        signer2.setRecipientId("2");
        signer2.setTabs(EnvelopeHelpers.createSignerTabs(signHere2));

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer1, signer2));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("EnvelopeWorkflowTest");
        envelope.setDocuments(Collections.singletonList(document));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);
        envelope.setWorkflow(workflow);

        return envelope;
    }
}