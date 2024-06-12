package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.PhoneAuthenticationService;
import com.docusign.core.common.Utils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Example 020: Phone Authentication for recipient
 */
@Controller
@RequestMapping("/eg020")
public class EG020ControllerPhoneAuthentication extends AbstractEsignatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EG023ControllerIdvAuthentication.class);

    public EG020ControllerPhoneAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg020", session, user);
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
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        if (args.getSignerEmail().equals(config.getSignerEmail())) {
            throw new ApiException(config.getCodeExamplesText().SupportingTexts.IdenticalEmailsNotAllowedErrorMessage);
        }

        String accountId = session.getAccountId();

        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        //ds-snippet-start:eSign20Step3
        AccountsApi workflowDetails = new AccountsApi(apiClient);
        AccountIdentityVerificationResponse workflowRes = workflowDetails.getAccountIdentityVerification(session.getAccountId());
        List<AccountIdentityVerificationWorkflow> identityVerification = workflowRes.getIdentityVerification();
        String workflowId = "";
        for (int i = 0; i < identityVerification.size(); i++) {
            if (identityVerification.get(i).getDefaultName().equals("Phone Authentication")) {
                workflowId = identityVerification.get(i).getWorkflowId();
            }
        }
        //ds-snippet-end:eSign20Step3
        LOGGER.info("workflowId = " + workflowId);
        if (workflowId.equals("")) {
            throw new ApiException(0, getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage);
        }
        //ds-snippet-start:eSign20Step5      
        EnvelopeDefinition envelope = PhoneAuthenticationService.createEnvelope(args.getSignerName(), args.getSignerEmail(), args.getCountryCode(),
                args.getPhoneNumber(), workflowId);
        //ds-snippet-end:eSign20Step5      
        //ds-snippet-start:eSign20Step4
        EnvelopeSummary results = PhoneAuthenticationService.phoneAuthentication(envelopesApi, accountId, envelope);
        //ds-snippet-end:eSign20Step4

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
