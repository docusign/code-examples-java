package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.CreateTemplateService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeTemplate;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Create a template.<br />
 * Create a template with two roles, <b>signer</b> and <b>cc</b>. The template
 * includes three documents.
 */
@Controller
@RequestMapping("/eg008")
public class EG008ControllerCreateTemplate extends AbstractEsignatureController {

    private static final String TEMPLATE_NAME = "Example Signer and CC template";

    public EG008ControllerCreateTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg008", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        // Step 1. list existing templates
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        String accountId = session.getAccountId();
        EnvelopeTemplateResults envelopeTemplateResults = CreateTemplateService.searchTemplatesByName(
                apiClient,
                accountId,
                TEMPLATE_NAME);

        // Step 2. Process results. If template do not exist, create one

        if (Integer.parseInt(envelopeTemplateResults.getResultSetSize()) > 0) {
            EnvelopeTemplate template = envelopeTemplateResults.getEnvelopeTemplates().get(0);
            session.setTemplateId(template.getTemplateId());

            DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                    .withMessage(
                            "The template already exists in your account." + getTextForCodeExampleByApiType().ResultsPageText
                                    .replaceFirst("\\{0}", template.getName()).replaceFirst("\\{1}", template.getTemplateId()))
                    .addToModel(model, config);
        } else {
            session.setTemplateName(TEMPLATE_NAME);

            //ds-snippet-start:eSign8Step3
            TemplateSummary template = CreateTemplateService.createTemplate(apiClient, accountId, CreateTemplateService.makeTemplate("Example Signer and CC template"));
            //ds-snippet-end:eSign8Step3
            session.setTemplateId(template.getTemplateId());
            DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                    .withMessage(
                            "The template has been created!" + getTextForCodeExampleByApiType().ResultsPageText
                                    .replaceFirst("\\{0}", template.getName()).replaceFirst("\\{1}", template.getTemplateId()))
                    .addToModel(model, config);
        }
        return DONE_EXAMPLE_PAGE;
    }
}
