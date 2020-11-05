package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Get an envelope's basic information and status.<br />
 * List the basic information about an envelope, including its overall status.
 * Additional API/SDK methods may be used to get additional information about
 * the envelope, its documents, recipients, etc. This example demonstrates
 * how to obtain the latest information about an envelope from DocuSign. Often
 * an alternative is to use Connect to enable DocuSign to proactively send your
 * application updates when the status of an envelope changes.
 */
@Controller
@RequestMapping("/eg004")
public class EG004ControllerEnvelopeInfo extends AbstractEsignatureController {

    private final Session session;
    private final User user;


    @Autowired
    public EG004ControllerEnvelopeInfo(DSConfiguration config, Session session, User user) {
        super(config, "eg004", "Get envelope information");
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
        // Step 1. get envelope info
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        DoneExample.createDefault(title)
                .withJsonObject(envelopesApi.getEnvelope(session.getAccountId(), session.getEnvelopeId()))
                .withMessage("Results from the Envelopes::get method:")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
