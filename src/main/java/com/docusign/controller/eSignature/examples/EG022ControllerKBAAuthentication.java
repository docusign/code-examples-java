package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;

import com.docusign.services.eSignature.examples.KBAAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;


/**
 * Send an envelope with a recipient using Knowledge-Based Authentication.
 */
@Controller
@RequestMapping("/eg022")
public class EG022ControllerKBAAuthentication extends AbstractEsignatureController {

    private final Session session;
    private final User user;


    @Autowired
    public EG022ControllerKBAAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg022", "Signing request by email");
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
        EnvelopeDefinition envelope = KBAAuthenticationService.createEnvelope(args.getSignerName(), args.getSignerEmail());

        // Step 3: Call the eSignature REST API
        EnvelopeSummary results = KBAAuthenticationService.kbaAuthentication(
                envelopesApi,
                session.getAccountId(),
                envelope
        );

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title)
                .withJsonObject(results)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                     + results.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    // ***DS.snippet.0.end
}
