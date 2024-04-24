package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.ResponsiveSigningService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ViewUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/eg038")
public class EG038ControllerResponsiveSigning extends AbstractEsignatureController {

    private static final Logger log = LoggerFactory.getLogger(EG038ControllerResponsiveSigning.class);

    private static final String SIGNER_CLIENT_ID = "1000";

    private static final String FEATURE_WAS_DISABLED = "Please enable this feature in the developer account and try again.";

    public EG038ControllerResponsiveSigning(DSConfiguration config, Session session, User user) {
        super(config, "eg038", session, user);
    }

    @Override
	//ds-snippet-start:eSign38Step3
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String ccName = args.getCcName();
        String ccEmail = args.getCcEmail();
        String accountId = session.getAccountId();
        ViewUrl viewUrl = null;

        EnvelopeDefinition envelope = ResponsiveSigningService.makeEnvelope(
                signerEmail,
                signerName,
                SIGNER_CLIENT_ID,
                ccEmail,
                ccName,
                args);

        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        try {
            EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelope);

            String envelopeId = envelopeSummary.getEnvelopeId();

            RecipientViewRequest viewRequest = ResponsiveSigningService.makeRecipientViewRequest(
                    signerEmail,
                    signerName,
                    config,
                    SIGNER_CLIENT_ID);

            viewUrl = ResponsiveSigningService.responsiveSigning(
                    envelopesApi,
                    accountId,
                    envelopeId,
                    viewRequest);

        } catch (ApiException apiException) {
            log.error(apiException.getMessage());
            DoneExample.createDefault(this.title)
                    .withMessage("Feature Not Enabled")
                    .withFixingInstructions(FEATURE_WAS_DISABLED)
                    .addToModel(model, config);
            return ERROR_PAGE;
        }

        return new RedirectView(viewUrl.getUrl());
    }
	//ds-snippet-end:eSign38Step3
}
