package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.MultipleDeliveryService;
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
 * Send an envelope with a remote signer and cc recipient o be notified via multiple 
 * delivery channels (Email and SMS or WhatsApp).<br />
 * The envelope includes a pdf, Word, and HTML document. Anchor text is used to
 * position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg046")
public class EG046ControllerMultipleDelivery extends AbstractEsignatureController {

	public EG046ControllerMultipleDelivery(DSConfiguration config, Session session, User user) {
		super(config, "eg046", session, user);
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
        args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

		try{
			EnvelopeDefinition envelope = MultipleDeliveryService.makeEnvelope(
				args.getSignerName(),
				args.getSignerEmail(),
				args.getCountryCode(),
				args.getPhoneNumber(),
				args.getCcName(),
				args.getCcEmail(),
				args.getCcCountryCode(),
				args.getCcPhoneNumber(),
				args.getDeliveryMethod(),
				args.getStatus());
				
			EnvelopeSummary envelopeSummary = SMSDeliveryService.smsDelivery(
				envelopesApi,
				session.getAccountId(),
				envelope);

			session.setEnvelopeId(envelopeSummary.getEnvelopeId());
			DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
				.withMessage(getTextForCodeExampleByApiType().ResultsPageText
					.replaceFirst("\\{0}", envelopeSummary.getEnvelopeId())
				)
				.addToModel(model, config);
			return DONE_EXAMPLE_PAGE;
		} catch (ApiException e) {
            new DoneExample()
				.withTitle(exampleName)
				.withName(title)
				.withFixingInstructions(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage)
				.addToModel(model, config);

            return ERROR_PAGE;
        }
    }
}
