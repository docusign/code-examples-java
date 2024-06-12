package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.IdvAuthenticationService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EG023ControllerIdvAuthentication.class);

    public EG023ControllerIdvAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg023", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException, IOException {
        if (args.getSignerEmail().equals(config.getSignerEmail())) {
            throw new ApiException(config.getCodeExamplesText().SupportingTexts.IdenticalEmailsNotAllowedErrorMessage);
        }

        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        String workflowId = IdvAuthenticationService.retrieveWorkflowId(apiClient, session.getAccountId());
        LOGGER.info("workflowId = " + workflowId);
        if (workflowId.equals("")) {
            throw new ApiException(0, getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage);
        }
        EnvelopeDefinition envelope = IdvAuthenticationService.createEnvelope(args.getSignerName(), args.getSignerEmail(), workflowId);
        //ds-snippet-start:eSign23Step5
        EnvelopeSummary results = IdvAuthenticationService.idvAuthentication(envelopesApi, session.getAccountId(), envelope);
        //ds-snippet-end:eSign23Step5

        session.setEnvelopeId(results.getEnvelopeId());
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(results)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", results.getEnvelopeId())
                )
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
