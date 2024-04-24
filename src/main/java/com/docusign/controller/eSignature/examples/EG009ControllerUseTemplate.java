package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.UseTemplateService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


/**
 * Send an envelope using a template.<br />
 * The envelope is defined by the template. The signer and cc recipient name
 * and email are used to fill in the template's <em>roles</em>. This example
 * demonstrates a common pattern for DocuSign integrations: envelopes will be
 * sent programmatically, based on a template. If the template definition needs
 * to be updated, the DocuSign web tool can be used to easily update the
 * template, thus avoiding the need to make software changes.
 */
@Controller
@RequestMapping("/eg009")
public class EG009ControllerUseTemplate extends AbstractEsignatureController {

    private static final String MODEL_LIST_TEMPLATE = "listTemplates";

    public EG009ControllerUseTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg009", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopeTemplateResults templates = UseTemplateService.listTemplates(apiClient, session.getAccountId());
        model.addAttribute(MODEL_LIST_TEMPLATE, templates.getEnvelopeTemplates());
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        EnvelopeDefinition envelope = UseTemplateService.makeEnvelope(
                args.getSignerName(),
                args.getSignerEmail(),
                args.getCcEmail(),
                args.getCcName(),
                args.getTemplateId()
        );
        EnvelopeSummary envelopeSummary = UseTemplateService.createEnvelopeTemplate(
                envelopesApi,
                session.getAccountId(),
                envelope);

        session.setEnvelopeId(envelopeSummary.getEnvelopeId());
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(envelopeSummary)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeSummary.getEnvelopeId())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
