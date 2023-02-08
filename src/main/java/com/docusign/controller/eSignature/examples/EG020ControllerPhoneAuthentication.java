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
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientIdentityInputOption;
import com.docusign.esign.model.RecipientIdentityPhoneNumber;
import com.docusign.esign.model.RecipientIdentityVerification;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Example 020: Phone Authentication for recipient
 */
@Controller
@RequestMapping("/eg020")
public class EG020ControllerPhoneAuthentication extends AbstractEsignatureController {

    private static final Logger logger = LoggerFactory.getLogger(EG023ControllerIdvAuthentication.class);
    private final Session session;
    private final User user;

    @Autowired
    public EG020ControllerPhoneAuthentication(DSConfiguration config, Session session, User user) {
        super(config, "eg020");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
      if(Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId())){
        session.setStatusCFR("enabled");
        throw new Exception(config.getCodeExamplesText().getSupportingTexts().getCFRError());
      }
        super.onInitModel(args, model);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        String accountId = session.getAccountId();

        // Step 2 start
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        // Step 2 end
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 3 start
        AccountsApi workflowDetails = new AccountsApi(apiClient);
        AccountIdentityVerificationResponse workflowRes = workflowDetails.getAccountIdentityVerification(session.getAccountId());
        List<AccountIdentityVerificationWorkflow> identityVerification = workflowRes.getIdentityVerification();
        String workflowId = "";
        for (int i = 0; i < identityVerification.size(); i++)
        {
            if (identityVerification.get(i).getDefaultName().equals("Phone Authentication"))
            {
                workflowId = identityVerification.get(i).getWorkflowId();
            }
        }
        // Step 3 end
        logger.info("workflowId = " + workflowId);
        if (workflowId.equals(""))
        {
            throw new ApiException(0, getTextForCodeExample().CustomErrorTexts.get(0).ErrorMessage);
        }EnvelopeDefinition envelope = PhoneAuthenticationService.createEnvelope(args.getSignerName(), args.getSignerEmail(), args.getCountryCode(),
                args.getPhoneNumber(), workflowId);
        // Step 4.1 start
        EnvelopeSummary results = PhoneAuthenticationService.phoneAuthentication(envelopesApi, accountId, envelope);
        // Step 4.1 end

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
