package com.docusign;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.AbstractEsignatureController;
import com.docusign.controller.eSignature.services.EmbeddedSigningService;
import com.docusign.core.common.Utils;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ViewUrl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;


/**
 * Use embedded signing.
 * This example sends an envelope, and then uses embedded signing
 * for the first signer. Embedded signing provides a smoother user experience
 * for the signer: the DocuSign signing is initiated from your site.
 */
@Controller
@RequestMapping("/eg001")
public class EG001ControllerEmbeddedSigning extends AbstractEsignatureController {

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

    private static final String DOCUMENT_NAME = "Lorem Ipsum";

    private static final int ANCHOR_OFFSET_Y = 20;

    private static final int ANCHOR_OFFSET_X = 10;

    private static final String SIGNER_CLIENT_ID = "1000";

    public EG001ControllerEmbeddedSigning(DSConfiguration config, Session session, User user) {
        super(config, Boolean.valueOf(config.getQuickACG()) ? "quickEmbeddedSigning" : "eg001", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        if (Utils.isCfr(session.getBasePath(), user.getAccessToken(), session.getAccountId())) {
            session.setStatusCFR("enabled");
            throw new Exception(config.getCodeExamplesText().getSupportingTexts().getCFRError());
        }
        if (config.getQuickstart().equals("true")) {
            config.setQuickstart("false");
        }
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws Exception {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String accountId = session.getAccountId();

        // Create the envelope definition
        //ds-snippet-start:eSign1Step3
        EnvelopeDefinition envelope = EmbeddedSigningService.makeEnvelope(
                signerEmail,
                signerName,
                SIGNER_CLIENT_ID,
                ANCHOR_OFFSET_Y,
                ANCHOR_OFFSET_X,
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME);

        // Call DocuSign to create the envelope
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        String envelopeId = EmbeddedSigningService.createEnvelope(apiClient, session.getAccountId(), envelope);
        //ds-snippet-end
        session.setEnvelopeId(envelopeId);

        // Create the recipient view, the embedded signing
        //ds-snippet-start:eSign1Step5
        RecipientViewRequest viewRequest = EmbeddedSigningService.makeRecipientViewRequest(
                signerEmail,
                signerName,
                SIGNER_CLIENT_ID,
                config.getDsReturnUrl(),
                config.getDsPingUrl());

        ViewUrl viewUrl = EmbeddedSigningService.embeddedSigning(
                apiClient,
                accountId,
                envelopeId,
                viewRequest
        );

        // Redirect the user to the embedded signing
        // Don't use an iFrame!
        // State can be stored/recovered using the framework's session or a
        // query parameter on the returnUrl (see the makeRecipientViewRequest method)
        return new RedirectView(viewUrl.getUrl());
        //ds-snippet-end
    }
}
