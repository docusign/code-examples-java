package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.PauseSignatureWorkflowService;
import com.docusign.core.common.Utils;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This topic demonstrates how to create an envelope where the workflow
 * is paused before the envelope is sent to a second recipient.
 */
@Controller
@RequestMapping("/eg032")
public class EG032ControllerPauseSignatureWorkflow extends AbstractEsignatureController {

    public EG032ControllerPauseSignatureWorkflow(DSConfiguration config, Session session, User user) {
        super(config, "eg032", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        if (Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId())) {
            session.setStatusCFR("enabled");
            throw new Exception(config.getCodeExamplesText().getSupportingTexts().getCFRError());
        }
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {

        //ds-snippet-start:eSign32Step2
        EnvelopesApi envelopesApi = createEnvelopesApi(this.session.getBasePath(), this.user.getAccessToken());
        //ds-snippet-end:eSign32Step2
        
        // Construct your envelope JSON body
        EnvelopeDefinition envelope = PauseSignatureWorkflowService.createEnvelope(
                args.getSignerName(),
                args.getSignerEmail(),
                args.getSignerName2(),
                args.getSignerEmail2()
        );

        //ds-snippet-start:eSign32Step4
        EnvelopeSummary envelopeSummary = PauseSignatureWorkflowService.pauseSignatureWorkflow(
                envelopesApi,
                this.session.getAccountId(),
                envelope
        );
        //ds-snippet-end:eSign32Step4

        this.session.setEnvelopeId(envelopeSummary.getEnvelopeId());
        DoneExample.createDefault(this.title)
                .withJsonObject(envelopeSummary)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", this.session.getEnvelopeId())
                )
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}