package com.docusign.controller.webForms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.CreateTemplateService;
import com.docusign.controller.webForms.services.CreateRemoteInstanceService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeTemplate;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateSummary;
import com.docusign.webforms.model.WebFormInstance;
import com.docusign.webforms.model.WebFormSummaryList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/web002")
public class WEB002CreateRemoteInstance extends AbstractWebFormsController {

    private static final String TEMPLATE_ID = "templateId";

    private static final String TEMPLATE_NAME = "Web Form Example Template";

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_Web_Form.pdf";

    private static final String WEB_FORM_CONFIG = "web-form-config.json";

    private final DSConfiguration configuration;

    public WEB002CreateRemoteInstance(DSConfiguration config, Session session, User user) {
        super(config, "web002", session, user);
        this.configuration = config;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(TEMPLATE_ID, session.getWebformTemplateId());
    }

    @Override
    protected Object doWork(
            WorkArguments args,
            ModelMap model,
            HttpServletResponse response) throws ApiException, IOException, com.docusign.webforms.client.ApiException {
        if (session.getIsWebFormsInitialRun()) {
            handleInitialRun(model);
            return new RedirectView("web002");
        }

        String accountId = session.getAccountId();
        var apiClient = createWebFormsApiClient(
                config.getWebFormsBasePath(),
                user.getAccessToken());

        WebFormSummaryList forms = CreateRemoteInstanceService.getFormsByName(
                apiClient,
                accountId,
                TEMPLATE_NAME);

        if (forms.getItems() == null || forms.getItems().isEmpty()) {
            throw new ApiException(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage);
        }

        String formId = forms.getItems().get(0).getId();

        WebFormInstance form = CreateRemoteInstanceService.createInstance(
                apiClient,
                accountId,
                formId,
                configuration.getSignerEmail(),
                configuration.getSignerName());

        session.setIsWebFormsInitialRun(true);
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", form.getEnvelopes().get(0).getId()).replaceFirst("\\{1}", form.getId())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }

    private void handleInitialRun(ModelMap model) throws ApiException, IOException {
        ApiClient apiClient = createESignApiClient(session.getBasePath(), user.getAccessToken());
        String accountId = session.getAccountId();

        String templateId = findOrCreateTemplate(apiClient, accountId);
        session.setWebformTemplateId(templateId);
        session.setIsWebFormsInitialRun(false);

        model.addAttribute(TEMPLATE_ID, templateId);
        CreateRemoteInstanceService.addTemplateIdToForm(WEB_FORM_CONFIG, templateId);
    }

    private String findOrCreateTemplate(ApiClient apiClient, String accountId) throws ApiException, IOException {
        EnvelopeTemplateResults templateResults = CreateTemplateService.searchTemplatesByName(
                apiClient,
                accountId,
                TEMPLATE_NAME);

        if (templateResults.getEnvelopeTemplates() != null && !templateResults.getEnvelopeTemplates().isEmpty()) {
            EnvelopeTemplate existingTemplate = templateResults.getEnvelopeTemplates().get(0);
            return existingTemplate.getTemplateId();
        }

        TemplateSummary newTemplate = CreateTemplateService.createTemplate(
                apiClient,
                accountId,
                CreateRemoteInstanceService.prepareEnvelopeTemplate(TEMPLATE_NAME, DOCUMENT_FILE_NAME)
        );
        return newTemplate.getTemplateId();
    }
}
