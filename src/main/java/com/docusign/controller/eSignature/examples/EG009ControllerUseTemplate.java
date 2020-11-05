package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;

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

    private final Session session;
    private final User user;


    @Autowired
    public EG009ControllerUseTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg009", "Envelope sent");
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
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        EnvelopeDefinition envelope = makeEnvelope(args);
        EnvelopeSummary result = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        session.setEnvelopeId(result.getEnvelopeId());
        DoneExample.createDefault(title)
                .withJsonObject(result)
                .withMessage("The envelope has been created and sent!<br/>Envelope ID "
                        + result.getEnvelopeId() + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    private static EnvelopeDefinition makeEnvelope(WorkArguments args) {
        TemplateRole signer = new TemplateRole();
        signer.setEmail(args.getSignerEmail());
        signer.setName(args.getSignerName());
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        TemplateRole cc = new TemplateRole();
        cc.setEmail(args.getCcEmail());
        cc.setName(args.getCcName());
        cc.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setTemplateId(args.getTemplateId());
        envelope.setTemplateRoles(Arrays.asList(signer, cc));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelope;
    }
    // ***DS.snippet.0.end
}
