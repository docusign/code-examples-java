package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopesInformation;
import com.docusign.controller.eSignature.services.ListEnvelopesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


/**
 * List envelopes in the user's account.<br />
 * List the envelopes created in the last 30 days. This example demonstrates
 * how to query DocuSign about envelopes sent by the current user.
 */
@Controller
@RequestMapping("/eg003")
public class EG003ControllerListEnvelopes extends AbstractEsignatureController {

    private final Session session;
    private final User user;

    @Autowired
    public EG003ControllerListEnvelopes(DSConfiguration config, Session session, User user) {
        super(config, "eg003");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        EnvelopesInformation envelopesInformation = ListEnvelopesServices.listEnvelopes(
                envelopesApi,
                session.getAccountId());

        DoneExample.createDefault(getTextForCodeExample(getAPITypeFromLink()).ExampleName)
                .withMessage(getTextForCodeExample(getAPITypeFromLink()).ResultsPageText)
                .withJsonObject(envelopesInformation)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
