package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountIdentityVerificationResponse;
import com.docusign.esign.model.AccountIdentityVerificationWorkflow;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.controller.eSignature.services.IdvAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        super(config, "eg023");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException, IOException {
        // Step 2 start
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        // Step 2 end
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        String workflowId = IdvAuthenticationService.retrieveWorkflowId(apiClient, session.getAccountId());
        logger.info("workflowId = " + workflowId);
        if (workflowId.equals(""))
        {
            throw new ApiException(0, getTextForCodeExample().CustomErrorTexts.get(0).ErrorMessage);
        }
        // Step 4-1 start
        EnvelopeDefinition envelope = IdvAuthenticationService.createEnvelope(args.getSignerName(), args.getSignerEmail(), workflowId);
        // Step 4-1 end
        // Step 5 start
        EnvelopeSummary results = IdvAuthenticationService.idvAuthentication(envelopesApi, session.getAccountId(), envelope);
        // Step 5 end

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(getTextForCodeExample().ExampleName)
                .withJsonObject(results)
                .withMessage(getTextForCodeExample().ResultsPageText
                        .replaceFirst("\\{0}", results.getEnvelopeId())
                )
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
