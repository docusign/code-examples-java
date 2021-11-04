package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.controller.eSignature.services.SMSDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eg035")
public class EG035ControllerSMSDelivery extends AbstractEsignatureController {

    private final Session session;
    private final User user;

    @Autowired
    public EG035ControllerSMSDelivery(DSConfiguration config, Session session, User user) {
        super(config, "eg035", "Signing request by email");
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

        EnvelopeDefinition envelope = SMSDeliveryService.makeEnvelope(
                args.getSignerName(),
                args.getSignerEmail(),
                args.getCountryCode(),
                args.getPhoneNumber(),
                args.getCcEmail(),
                args.getCcName(),
                args.getCcCountryCode(),
                args.getCcPhoneNumber(),
                args.getStatus(),
                args
        );
        EnvelopeSummary envelopeSummary = SMSDeliveryService.smsDelivery(
                envelopesApi,
                session.getAccountId(),
                envelope
        );

        // process results
        session.setEnvelopeId(envelopeSummary.getEnvelopeId());
        DoneExample.createDefault(title)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                        + envelopeSummary.getEnvelopeId() + ".")
                .withJsonObject(envelopeSummary)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
