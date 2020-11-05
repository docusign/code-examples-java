package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Session session;
    private final User user;


    @Autowired
    public EG015ControllerGetTabValues(DSConfiguration config, Session session, User user) {
        super(config, "eg015", "Get Envelope Tabs");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_ENVELOPE_OK, StringUtils.isNotBlank(session.getEnvelopeId()));
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        // Step 1. get envelope recipients
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        DoneExample.createDefault(title)
                .withJsonObject(envelopesApi.getFormData(session.getAccountId(), session.getEnvelopeId()))
                .withMessage("Results from the Envelope::GetFormData method:")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
