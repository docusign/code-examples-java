package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.EnvelopeRecipientsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Recipients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


/**
 * List an envelope's recipients and their status.<br />
 * List the envelope's recipients, including their current status.
 */
@Controller
@RequestMapping("/eg005")
public class EG005ControllerEnvelopeRecipients extends AbstractEsignatureController {

    public EG005ControllerEnvelopeRecipients(DSConfiguration config, Session session, User user) {
        super(config, "eg005", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_ENVELOPE_OK, StringUtils.isNotBlank(session.getEnvelopeId()));
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        // Get envelope recipients
        //ds-snippet-start:eSign5Step2
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        Recipients recipients = EnvelopeRecipientsService.envelopeRecipients(
                envelopesApi,
                session.getAccountId(),
                session.getEnvelopeId());
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(recipients)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
        //ds-snippet-end:eSign5Step2
    }
}
