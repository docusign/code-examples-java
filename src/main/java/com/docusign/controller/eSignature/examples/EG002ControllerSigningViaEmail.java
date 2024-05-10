package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.SigningViaEmailService;
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
@RequestMapping("/eg002")
public class EG002ControllerSigningViaEmail extends AbstractEsignatureController {

    public EG002ControllerSigningViaEmail(DSConfiguration config, Session session, User user) {
        super(config, "eg002", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Set status for the makeEnvelope method
        if (!EnvelopeHelpers.ENVELOPE_STATUS_CREATED.equalsIgnoreCase(args.getStatus())) {
            args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        }

        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        EnvelopeDefinition envelope = SigningViaEmailService.makeEnvelope(
                args.getSignerEmail(),
                args.getSignerName(),
                args.getCcEmail(),
                args.getCcName(),
                args.getStatus(),
                args
        );
        EnvelopeSummary envelopeSummary = SigningViaEmailService.signingViaEmail(
                envelopesApi,
                session.getAccountId(),
                envelope);

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
