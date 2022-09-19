package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeUpdateSummary;
import com.docusign.controller.eSignature.services.UnpauseSignatureWorkflowService;
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
        super(config, "eg033");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {

        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        EnvelopeUpdateSummary envelopeUpdateSummary = UnpauseSignatureWorkflowService.unpauseSignatureWorkflow(
                envelopesApi,
                this.session.getAccountId(),
                this.session.getEnvelopeId()
        );

        DoneExample.createDefault(this.title)
                .withJsonObject(envelopeUpdateSummary)
                .withMessage(getTextForCodeExample().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
