package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.services.eSignature.IdvAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ID Verification authentication
 */
@Controller
@RequestMapping("/eg023")
public class EG023ControllerIdvAuthentication extends AbstractEsignatureController {

    private static final Logger logger = LoggerFactory.getLogger(EG023ControllerIdvAuthentication.class);
    private final Session session;
    private final User user;

    @Autowired
    public EG023ControllerIdvAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg023", "ID Verification Authentication");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException
    {
        // Step 1: Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        //Step 2: Retrieve the workflow ID
        String workflowId = IdvAuthenticationService.retrieveWorkflowId(apiClient, session.getAccountId());

        logger.info("workflowId = " + workflowId);
        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = IdvAuthenticationService.createEnvelope(
                args.getSignerName(),
                args.getSignerEmail(),
                workflowId);
        // Step 4: Create envelope
        EnvelopeSummary results = IdvAuthenticationService.idvAuthentication(
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
}
