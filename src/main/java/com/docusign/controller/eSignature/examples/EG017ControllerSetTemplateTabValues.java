package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.SetTemplateTabValuesService;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/eg017")
public class EG017ControllerSetTemplateTabValues extends AbstractEsignatureController {

    private static final String MODEL_LIST_TEMPLATE = "listTemplates";

    private static final String SIGNER_CLIENT_ID = "1000";

    public EG017ControllerSetTemplateTabValues(DSConfiguration config, Session session, User user) {
        super(config, "eg017", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        TemplatesApi templatesApi = new TemplatesApi(apiClient);
        EnvelopeTemplateResults templates = templatesApi.listTemplates(session.getAccountId());
        model.addAttribute(MODEL_LIST_TEMPLATE, templates.getEnvelopeTemplates());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String accountId = session.getAccountId();
        String ccName = args.getCcName();
        String ccEmail = args.getCcEmail();
        String templateId = args.getTemplateId();

        // Step 2. Construct your API headers
        //ds-snippet-start:eSign17Step2
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        //ds-snippet-end:eSign17Step2
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 3. is shown in the makeEnvelope method below
        // Step 4. Construct your request body
        EnvelopeDefinition envelope = SetTemplateTabValuesService.makeEnvelope(
                signerEmail,
                signerName,
                ccEmail,
                ccName,
                templateId,
                SIGNER_CLIENT_ID);

        // Step 5. Call the eSignature REST API
        //ds-snippet-start:eSign17Step5
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelope);
        String envelopeId = envelopeSummary.getEnvelopeId();
        session.setEnvelopeId(envelopeId);
        //ds-snippet-end:eSign17Step5

        // Step 6. Create the view request
        RecipientViewRequest viewRequest = SetTemplateTabValuesService.makeRecipientViewRequest(
                signerEmail,
                signerName,
                config.getDsReturnUrl(),
                SIGNER_CLIENT_ID,
                config.getDsPingUrl());

        ViewUrl viewUrl = SetTemplateTabValuesService.setTemplateTabValues(
                envelopesApi,
                accountId,
                envelopeId,
                viewRequest
        );

        // State can be stored/recovered using the framework's session or a
        // query parameter on the returnUrl (see the makeRecipientViewRequest method)
        return new RedirectView(viewUrl.getUrl());
    }
}
