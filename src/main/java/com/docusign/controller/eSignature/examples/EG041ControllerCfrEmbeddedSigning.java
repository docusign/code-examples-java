package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
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
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ViewUrl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.docusign.controller.eSignature.services.CfrEmbeddedSigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;


/**
 * Use embedded signing.<br/>
 * This example sends an envelope, and then uses embedded signing
 * for the first signer. Embedded signing provides a smoother user experience
 * for the signer: the DocuSign signing is initiated from your site.
 */
@Controller
@RequestMapping("/eg041")
public class EG041ControllerCfrEmbeddedSigning extends AbstractEsignatureController {

    private static final Logger logger = LoggerFactory.getLogger(EG023ControllerIdvAuthentication.class);
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";
    private static final String DOCUMENT_NAME = "Lorem Ipsum";
    private static final int ANCHOR_OFFSET_Y = -30;
    private static final int ANCHOR_OFFSET_X = 10;
    private final Session session;
    private final User user;

    @Autowired
    public EG041ControllerCfrEmbeddedSigning(DSConfiguration config, Session session, User user){
        super(config, "eg041");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws Exception {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String signerClientId = args.getSignerEmail();
        String countryCode = args.getCountryCode();
        String phoneNumber = args.getPhoneNumber();
        String accountId = session.getAccountId();

        // Step 2 start
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        // Step 2 end

        // Step 3 start
        AccountsApi workflowDetails = new AccountsApi(apiClient);
        AccountIdentityVerificationResponse workflowRes = workflowDetails.getAccountIdentityVerification(session.getAccountId());
        List<AccountIdentityVerificationWorkflow> identityVerification = workflowRes.getIdentityVerification();
        String workflowId = "";
        for (int i = 0; i < identityVerification.size(); i++)
        {
            if (identityVerification.get(i).getDefaultName().equals("SMS for access & signatures"))
            {
                workflowId = identityVerification.get(i).getWorkflowId();
            }
        }
        // Step 3 end
        logger.info("workflowId = " + workflowId);
        if (workflowId.equals(""))
        {
            throw new ApiException(0, getTextForCodeExample().CustomErrorTexts.get(0).ErrorMessage);
        }


        // Step 1. Create the envelope definition
        EnvelopeDefinition envelope = CfrEmbeddedSigningService.makeEnvelope(
                signerName,
                signerEmail,
                countryCode,
                phoneNumber,
                workflowId,
                signerClientId,
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME);

        // Step 2. Call DocuSign to create the envelope
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelope);

        String envelopeId = envelopeSummary.getEnvelopeId();
        session.setEnvelopeId(envelopeId);

        // Step 3. create the recipient view, the embedded signing
        RecipientViewRequest viewRequest = CfrEmbeddedSigningService.makeRecipientViewRequest(
                signerEmail,
                signerName,
                config,
                signerClientId);

        ViewUrl viewUrl = CfrEmbeddedSigningService.embeddedSigning(
                envelopesApi,
                accountId,
                envelopeId,
                viewRequest
        );

        // Step 4. Redirect the user to the embedded signing
        // Don't use an iFrame!
        // State can be stored/recovered using the framework's session or a
        // query parameter on the returnUrl (see the makeRecipientViewRequest method)
        return new RedirectView(viewUrl.getUrl());
    }
}
