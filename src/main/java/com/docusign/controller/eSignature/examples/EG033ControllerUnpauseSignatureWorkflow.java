package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeUpdateSummary;
import com.docusign.esign.model.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


/**
 * This topic demonstrates how to resume an envelope workflow that has been paused.
 */
@Controller
@RequestMapping("/eg033")
public class EG033ControllerUnpauseSignatureWorkflow extends AbstractEsignatureController {

    private final Session session;
    private final User user;


    @Autowired
    public EG033ControllerUnpauseSignatureWorkflow(DSConfiguration config, Session session, User user) {
        super(config, "eg033", "Unpause a signature workflow");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {

        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        Workflow workflow = new Workflow();
        workflow.setWorkflowStatus(EnvelopeHelpers.WORKFLOW_STATUS_IN_PROGRESS);

        Envelope envelope = new Envelope();
        envelope.setWorkflow(workflow);

        EnvelopesApi.UpdateOptions updateOptions = envelopesApi. new UpdateOptions();
        updateOptions.setResendEnvelope("true");

        // Step 4: Call the eSignature REST API
        EnvelopeUpdateSummary results = envelopesApi.update(
                this.session.getAccountId(),
                this.session.getEnvelopeId(),
                envelope,
                updateOptions
        );

        DoneExample.createDefault(this.title)
                .withJsonObject(results)
                .withMessage("A paused envelope was resumed.")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
