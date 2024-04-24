package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.GetTabValuesService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeFormData;
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
@RequestMapping("/eg015")
public class EG015ControllerGetTabValues extends AbstractEsignatureController {

    public EG015ControllerGetTabValues(DSConfiguration config, Session session, User user) {
        super(config, "eg015", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_ENVELOPE_OK, StringUtils.isNotBlank(session.getEnvelopeId()));
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        //ds-snippet-start:eSign15Step2
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        //ds-snippet-end:eSign15Step2
        //ds-snippet-start:eSign15Step3
        EnvelopeFormData envelopeFormData = GetTabValuesService.getTabValues(
                envelopesApi,
                session.getAccountId(),
                session.getEnvelopeId());
        //ds-snippet-end:eSign15Step3
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(envelopeFormData)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
