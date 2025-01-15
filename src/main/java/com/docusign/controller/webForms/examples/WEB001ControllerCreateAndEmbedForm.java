package com.docusign.controller.webForms.examples;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.CreateTemplateService;
import com.docusign.controller.webForms.services.CreateAndEmbedFormService;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeTemplate;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateSummary;
import com.docusign.webforms.model.WebFormInstance;
import com.docusign.webforms.model.WebFormSummaryList;

@Controller
@RequestMapping("/web001")
public class WEB001ControllerCreateAndEmbedForm extends AbstractWebFormsController {

    private static final String TEMPLATE_ID = "templateId";

    private static final String TEMPLATE_NAME = "Web Form Example Template";

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_Web_Form.pdf";

    private static final String WEB_FORM_CONFIG = "web-form-config.json";

    public static final String EMBED = "pages/webforms/examples/embed";

    public static final String INSTANCE_TOKEN = "instanceToken";

    private static final String EMDEDDED_BY_TEXT = "example";

    public static final String URL = "url";

    public static final String INTEGRATION_KEY = "integrationKey";

    public WEB001ControllerCreateAndEmbedForm(DSConfiguration config, Session session, User user) {
        super(config, "web001", session, user);
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
        if (session.getWebformTemplateId() == null) {
            ApiClient eSignApiClient = createESignApiClient(session.getBasePath(), user.getAccessToken());
            String accountId = session.getAccountId();

            EnvelopeTemplateResults envelopeTemplateResults = CreateTemplateService.searchTemplatesByName(
                    eSignApiClient,
                    accountId,
                    TEMPLATE_NAME);

            if (Integer.parseInt(envelopeTemplateResults.getResultSetSize()) > 0) {
                EnvelopeTemplate template = envelopeTemplateResults.getEnvelopeTemplates().get(0);
                session.setWebformTemplateId(template.getTemplateId());
            } else {
                TemplateSummary template = CreateTemplateService.createTemplate(
                        eSignApiClient,
                        accountId,
                        CreateAndEmbedFormService.prepareEnvelopeTemplate(TEMPLATE_NAME, DOCUMENT_FILE_NAME));

                session.setWebformTemplateId(template.getTemplateId());
            }

            model.addAttribute(TEMPLATE_ID, session.getWebformTemplateId());
            CreateAndEmbedFormService.addTemplateIdToForm(WEB_FORM_CONFIG, session.getWebformTemplateId());

            return new RedirectView("web001");
        }

        var webFormsApiClient = createWebFormsApiClient(
                config.getWebFormsBasePath(),
                user.getAccessToken());

        WebFormSummaryList forms = CreateAndEmbedFormService.getForms(
                webFormsApiClient,
                session.getAccountId(),
                TEMPLATE_NAME);

        if (forms.getItems() == null || forms.getItems().size() == 0) {
            return new RedirectView("web001");
        }

        String formId = forms.getItems().get(0).getId();

        WebFormInstance form = CreateAndEmbedFormService.createInstance(
                webFormsApiClient,
                session.getAccountId(),
                formId);

        session.setWebformTemplateId(null);
        model.addAttribute(EMDEDDED_BY_TEXT, getTextForCodeExampleByApiType());
        model.addAttribute(LAUNCHER_TEXTS, config.getCodeExamplesText().SupportingTexts);
        model.addAttribute(INSTANCE_TOKEN, form.getInstanceToken());
        model.addAttribute(URL, form.getFormUrl());
        model.addAttribute(INTEGRATION_KEY, config.getUserId());

        return EMBED;
    }
}
