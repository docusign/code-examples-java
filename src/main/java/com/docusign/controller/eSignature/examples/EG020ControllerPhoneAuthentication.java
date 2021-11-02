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
import com.docusign.controller.eSignature.services.PhoneAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Example 020: Phone Authentication for recipient
 */
@Controller
@RequestMapping("/eg020")
public class EG020ControllerPhoneAuthentication extends AbstractEsignatureController {
    // For List.of you could even do groups of numbers such as
    // List.of("415-555-1212", "415-555-3434");
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "Lorem";

    private final Session session;
    private final User user;

    @Autowired
    public EG020ControllerPhoneAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg020", "Require Phone Authentication for a Recipient");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        String accountId = session.getAccountId();

        // Step 2 start
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        // Step 2 end

        // Step 2: Construct your envelope JSON body
        EnvelopeDefinition envelope = PhoneAuthenticationService.createEnvelope(
                args.getSignerName(),
                args.getSignerEmail(),
                args.getPhoneNumber());

        // Step 3: Call the eSignature REST API
        EnvelopeSummary results = PhoneAuthenticationService.phoneAuthentication(
                envelopesApi,
                session.getAccountId(),
                envelope
        );

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(title).withJsonObject(results)
                .withMessage(
                        "The envelope has been created and sent!<br />Envelope ID " + results.getEnvelopeId() + ".")
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }
}
