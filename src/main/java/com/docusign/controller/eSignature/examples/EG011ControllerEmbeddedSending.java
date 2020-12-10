package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.ReturnUrlRequest;
import com.docusign.esign.model.ViewUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;


/**
 * Use embedded sending<br />
 * An envelope will be created in draft mode. A browser will then be redirected
 * to the DocuSign web tool where the envelope can be (optionally) updated and
 * then sent. The envelope includes a pdf, Word, and HTML document.
 */
@Controller
@RequestMapping("/eg011")
public class EG011ControllerEmbeddedSending extends AbstractEsignatureController {

    private final Session session;
    private final User user;


    @Autowired
    public EG011ControllerEmbeddedSending(DSConfiguration config, Session session, User user) {
        super(config, "eg011", "Signing request by email");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
            HttpServletResponse response) throws ApiException, IOException {
        String accountId = session.getAccountId();
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 1. Make the envelope with "created" (draft) status
        args.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);
        EnvelopeDefinition env = EG002ControllerSigningViaEmail.makeEnvelope(args);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, env);
        String envelopeId = results.getEnvelopeId();

        // Step 2. Create the sender view.
        // Set the url where you want the recipient to go once they are done
        // signing should typically be a callback route somewhere in your app.
        ReturnUrlRequest viewRequest = new ReturnUrlRequest();
        viewRequest.setReturnUrl(config.getDsReturnUrl());
        ViewUrl viewUrl = envelopesApi.createSenderView(accountId, envelopeId, viewRequest);

        // Switch to Recipient and Documents view if requested by the user
        String url = viewUrl.getUrl();
        if ("recipient".equalsIgnoreCase(args.getStartingView())) {
            url = url.replace("send=1", "send=0");
        }

        return new RedirectView(url);
    }
    // ***DS.snippet.0.end
}
