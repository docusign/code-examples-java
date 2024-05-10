package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.SMSDeliveryService;
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
 * Send an envelope with a remote (email) signer and cc recipient.<br />
 * The envelope includes a pdf, Word, and HTML document. Anchor text is used to
 * position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg037")
public class EG037ControllerSMSDelivery extends AbstractEsignatureController {

    public EG037ControllerSMSDelivery(DSConfiguration config, Session session, User user) {
        super(config, "eg037", session, user);
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
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Set status for the makeEnvelope method
        if (!EnvelopeHelpers.ENVELOPE_STATUS_CREATED.equalsIgnoreCase(args.getStatus())) {
            args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        }

        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        EnvelopeDefinition envelope = SMSDeliveryService.makeEnvelope(
                args.getCountryCode(),
                args.getPhoneNumber(),
                args.getSignerName(),
                args.getDeliveryMethod(),
                args.getCcCountryCode(),
                args.getCcPhoneNumber(),
                args.getCcName(),
                args.getStatus()
        );
        EnvelopeSummary envelopeSummary = SMSDeliveryService.smsDelivery(
                envelopesApi,
                session.getAccountId(),
                envelope
        );

        // process results
        session.setEnvelopeId(envelopeSummary.getEnvelopeId());
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeSummary.getEnvelopeId())
                )
                .withJsonObject(envelopeSummary)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
