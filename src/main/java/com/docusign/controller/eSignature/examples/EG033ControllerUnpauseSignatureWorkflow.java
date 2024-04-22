package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.UnpauseSignatureWorkflowService;
import com.docusign.core.common.Utils;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeUpdateSummary;
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

    public EG033ControllerUnpauseSignatureWorkflow(DSConfiguration config, Session session, User user) {
        super(config, "eg033", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        if (Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId())) {
            session.setStatusCFR("enabled");
            throw new Exception(config.getCodeExamplesText().getSupportingTexts().getCFRError());
        }
        super.onInitModel(args, model);
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {

        // Step 2: Construct your API headers
        //ds-snippet-start:eSign33Step2
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        //ds-snippet-end:eSign33Step2

        EnvelopeUpdateSummary envelopeUpdateSummary = UnpauseSignatureWorkflowService.unpauseSignatureWorkflow(
                envelopesApi,
                this.session.getAccountId(),
                this.session.getEnvelopeId()
        );

        DoneExample.createDefault(this.title)
                .withJsonObject(envelopeUpdateSummary)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
