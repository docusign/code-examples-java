package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.common.DocumentType;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.CompositeTemplate;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.InlineTemplate;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.ServerTemplate;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.ViewUrl;

import com.docusign.services.eSignature.examples.AddDocToTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;


/**
 * Use embedded signing from a template with an added document. <br/>
 * This example sends an envelope based on a template. In addition to the
 * template's document(s), the example adds an additional document to the
 * envelope by using the Composite Templates.
 */
@Controller
@RequestMapping("/eg013")
public class EG013ControllerAddDocToTemplate extends AbstractEsignatureController {

    private static final String MODEL_LIST_TEMPLATE = "listTemplates";
    private static final String SIGNER_CLIENT_ID = "1000";

    private final Session session;
    private final User user;


    @Autowired
    public EG013ControllerAddDocToTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg013", "Use embedded signing from template and extra doc");
        this.session = session;
        this.user = user;
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
        String accountId = session.getAccountId();
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        ViewUrl viewUrl = AddDocToTemplateService.addDocumentToTemplate(
                envelopesApi,
                args,
                config.getDsReturnUrl(),
                config.getDsPingUrl(),
                SIGNER_CLIENT_ID,
                accountId
        );

        return new RedirectView(viewUrl.getUrl());
        // Step 4 end
    }


}
