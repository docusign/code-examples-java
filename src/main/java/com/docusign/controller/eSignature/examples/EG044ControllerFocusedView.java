package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.FocusedViewService;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


/**
 * Used to generate an envelope and allow user to sign it directly from the app without having to open an email.
 */
@Controller
@RequestMapping("/eg044")
public class EG044ControllerFocusedView extends AbstractEsignatureController {
    public static final String EMBED = "pages/esignature/examples/embed";

    public static final String INTEGRATION_KEY = "integrationKey";

    public static final String URL = "url";

    public static final String ENVELOPE_ID = "envelopeId";

    public static final String DOCUMENTATION = "documentation";

    public EG044ControllerFocusedView(DSConfiguration config, Session session, User user){
        super(config, "eg044", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws Exception {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String accountId = session.getAccountId();

        // Call DocuSign to create the envelope
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        // Call the method from Examples API to send envelope and generate url for focused view signing
        String[] envelopeIdAndRedirectUrl = new FocusedViewService().sendEnvelopeWithFocusedView(
                signerEmail,
                signerName,
                apiClient,
                accountId,
                config.getDsReturnUrl());

        if(envelopeIdAndRedirectUrl.length == 2){
            model.addAttribute(ENVELOPE_ID, envelopeIdAndRedirectUrl[0]);
            model.addAttribute(URL, envelopeIdAndRedirectUrl[1]);
        }

        model.addAttribute(DOCUMENTATION, config.getDocumentationPath() + exampleName);
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute(INTEGRATION_KEY, config.getUserId());

        return EMBED;
    }
}
