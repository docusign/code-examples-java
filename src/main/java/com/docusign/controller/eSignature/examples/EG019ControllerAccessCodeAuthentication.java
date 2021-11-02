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
import com.services.eSignature.AccessCodeAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Send an envelope with a recipient using Access Code Authentication. Signer
 * has to enter an access code for opening document to sign.
 */
@Controller
@RequestMapping("/eg019")
public class EG019ControllerAccessCodeAuthentication extends AbstractEsignatureController {

    private final Session session;
    private final User user;

    @Autowired
    public EG019ControllerAccessCodeAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg019", "Signing request by email");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Step 1: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 2: Construct your envelope JSON body
        EnvelopeDefinition envelope = AccessCodeAuthenticationService.createEnvelope(
                args.getSignerName(),
                args.getSignerEmail(),
                args.getAccessCode()
        );

        // Step 3: Call the eSignature REST API
        EnvelopeSummary results = AccessCodeAuthenticationService.accessCodeAuthentication(
                envelopesApi,
                session.getAccountId(),
                envelope
        );

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title)
                .withJsonObject(results)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                    + session.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
