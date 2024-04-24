package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.UseConditionalRecipientsService;
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
@RequestMapping("/eg034")
public class EG034ControllerUseConditionalRecipients extends AbstractEsignatureController {

    public EG034ControllerUseConditionalRecipients(DSConfiguration config, Session session, User user) {
        super(config, "eg034", session, user);
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
        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        //ds-snippet-start:eSign34Step3
        EnvelopeDefinition envelope = UseConditionalRecipientsService.createEnvelope(
                args.getSignerNotCheckedName(),
                args.getSignerNotCheckedEmail(),
                args.getSignerCheckedName(),
                args.getSignerCheckedEmail(),
                args.getSignerName(),
                args.getSignerEmail()
        );
        //ds-snippet-end:eSign34Step3

        // Step 4: Call the eSignature REST API
        try {
            EnvelopeSummary envelopeSummary = UseConditionalRecipientsService.useConditionalRecipients(
                    envelopesApi,
                    this.session.getAccountId(),
                    envelope
            );

            this.session.setEnvelopeId(envelopeSummary.getEnvelopeId());
            DoneExample.createDefault(this.title)
                    .withJsonObject(envelopeSummary)
                    .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                            .replaceFirst("\\{0}", this.session.getEnvelopeId()))
                    .addToModel(model, config);
        } catch (ApiException apiException) {
            if (!apiException.getMessage().contains(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessageCheck)) {
                throw apiException;
            }
            DoneExample.createDefault(this.title)
                    .withMessage(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage)
                    .addToModel(model, config);
        }

        return DONE_EXAMPLE_PAGE;
    }
}