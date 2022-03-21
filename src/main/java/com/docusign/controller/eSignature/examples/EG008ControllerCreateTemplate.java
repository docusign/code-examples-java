package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeTemplate;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateSummary;
import com.docusign.controller.eSignature.services.CreateTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Session session;
    private final User user;
    private static final String TEMPLATE_NAME = "Example Signer and CC template";

    @Autowired
    public EG008ControllerCreateTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg008", "Create a template");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
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

        if (Integer.parseInt(results.getResultSetSize()) > 0) {
            EnvelopeTemplate template = results.getEnvelopeTemplates().get(0);
            session.setTemplateId(template.getTemplateId());

            DoneExample.createDefault(title)
                    .withMessage(String.format(
                            "The template already exists in your account. <br/>Template name: %s, ID %s.",
                             template.getName(), template.getTemplateId()))
                    .addToModel(model);
        } else {
            session.setTemplateName(TEMPLATE_NAME);

            TemplateSummary template = templatesApi.createTemplate(accountId, makeTemplate());
            session.setTemplateId(template.getTemplateId());
            DoneExample.createDefault(title)
                    .withMessage(String.format(
                            "The template has been created!<br/>Template name: %s, ID %s.",
                            template.getName(), template.getTemplateId()))
                    .addToModel(model);
        }
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
