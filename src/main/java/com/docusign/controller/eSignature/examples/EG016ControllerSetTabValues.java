package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.SetTabValuesService;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ViewUrl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("/eg016")
public class EG016ControllerSetTabValues extends AbstractEsignatureController {

    public EG016ControllerSetTabValues(DSConfiguration config, Session session, User user) {
        super(config, "eg016", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String accountId = session.getAccountId();

        EnvelopeDefinition envelope = SetTabValuesService.makeEnvelope(signerEmail, signerName);

        //ds-snippet-start:eSign16Step2
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        //ds-snippet-end:eSign16Step2

        // Call the eSignature REST API
        //ds-snippet-start:eSign16Step4
        EnvelopeSummary envelopeSummary = SetTabValuesService.createEnvelope(envelopesApi, accountId, envelope);
        //ds-snippet-end:eSign16Step4                    

        String envelopeId = envelopeSummary.getEnvelopeId();
        session.setEnvelopeId(envelopeId);

        // Create the view request
        //ds-snippet-start:eSign16Step5
        RecipientViewRequest viewRequest = SetTabValuesService.makeRecipientViewRequest(
                signerEmail,
                signerName,
                config.getDsReturnUrl(),
                config.getDsPingUrl());
        ViewUrl viewUrl = SetTabValuesService.createRecipientView(envelopesApi, accountId, envelopeId, viewRequest);
        //ds-snippet-end:eSign16Step5

        // State can be stored/recovered using the framework's session or a
        // query parameter on the returnUrl (see the makeRecipientViewRequest method)
        return new RedirectView(viewUrl.getUrl());
    }
}
