package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.AccessCodeAuthenticationService;
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
 * Send an envelope with a recipient using Access Code Authentication. Signer
 * has to enter an access code for opening document to sign.
 */
@Controller
@RequestMapping("/eg019")
public class EG019ControllerAccessCodeAuthentication extends AbstractEsignatureController {

    public EG019ControllerAccessCodeAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg019", session, user);
    }

    @Override
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
        EnvelopeSummary envelopeSummary = AccessCodeAuthenticationService.accessCodeAuthentication(
                envelopesApi,
                session.getAccountId(),
                envelope
        );

        session.setEnvelopeId(envelopeSummary.getEnvelopeId());
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(envelopeSummary)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", session.getEnvelopeId())
                )
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
