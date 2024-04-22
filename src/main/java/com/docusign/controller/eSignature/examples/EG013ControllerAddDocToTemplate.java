package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.AddDocToTemplateService;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.ViewUrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Use embedded signing from a template with an added document. <br/>
 * This example sends an envelope based on a template. In addition to the
 * template's document(s), the example adds an additional document to the
 * envelope by using the Composite Templates.
 */
@Controller
@RequestMapping("/eg013")
public class EG013ControllerAddDocToTemplate extends AbstractEsignatureController {

    private final String SIGNER_CLIENT_ID = "1000";

    public EG013ControllerAddDocToTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg013", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_TEMPLATE_OK, StringUtils.isNotBlank(session.getTemplateId()));
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {

        // Set status for the makeEnvelope method
        if (!EnvelopeHelpers.ENVELOPE_STATUS_CREATED.equalsIgnoreCase(args.getStatus())) {
            args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        }

        String accountId = session.getAccountId();
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        ViewUrl embeddedEnvelope = AddDocToTemplateService.addDocumentToTemplate(
                envelopesApi,
                args.getSignerEmail(),
                args.getSignerName(),
                SIGNER_CLIENT_ID,
                args.getCcEmail(),
                args.getCcName(),
                session.getTemplateId(),
                accountId,
                config.getDsReturnUrl(),
                config.getDsPingUrl(),
                args
        );
        return new RedirectView(embeddedEnvelope.getUrl());
    }
}
