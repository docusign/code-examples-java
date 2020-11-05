package com.docusign.controller.eSignature.examples;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docusign.DSConfiguration;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BrandsResponse;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateRole;


/**
 * Applying a brand to a template.<br />
 * This code example demonstrates how to apply a brand to an template. The
 * envelope is defined by the template. The signer and cc recipient name and
 * email are used to fill in the template's <em>roles</em>. This example
 * demonstrates a common pattern for DocuSign integrations: envelopes will be
 * sent programmatically, based on a template. If the template definition needs
 * to be updated, the DocuSign web tool can be used to easily update the
 * template, thus avoiding the need to make software changes.
 */
@Controller
@RequestMapping("/eg030")
public class EG030ControllerApplyBrandToTemplate extends AbstractEsignatureController {

    private static final String MODEL_LIST_BRAND = "listBrands";
    private static final String MODEL_LIST_TEMPLATE = "listTemplates";

    private final Session session;
    private final User user;


    @Autowired
    public EG030ControllerApplyBrandToTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg030", "Apply brand to template");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        String accountId = session.getAccountId();
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        AccountsApi accountsApi = new AccountsApi(apiClient);
        BrandsResponse brands = accountsApi.listBrands(accountId);
        model.addAttribute(MODEL_LIST_BRAND, brands.getBrands());

        TemplatesApi templatesApi = new TemplatesApi(apiClient);
        EnvelopeTemplateResults templates = templatesApi.listTemplates(accountId);
        model.addAttribute(MODEL_LIST_TEMPLATE, templates.getEnvelopeTemplates());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = makeEnvelope(args);

        // Step 5: Call the eSignature REST API
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        DoneExample.createDefault(title)
                .withJsonObject(envelopeSummary)
                .withMessage("The envelope has been created and sent!<br />Envelope ID "
                    + envelopeSummary.getEnvelopeId() + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    private static EnvelopeDefinition makeEnvelope(WorkArguments args) {
        TemplateRole signer = new TemplateRole()
                .email(args.getSignerEmail())
                .name(args.getSignerName())
                .roleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        TemplateRole cc = new TemplateRole()
                .email(args.getCcEmail())
                .name(args.getCcName())
                .roleName(EnvelopeHelpers.CC_ROLE_NAME);

        return new EnvelopeDefinition()
                .templateId(args.getTemplateId())
                .templateRoles(Arrays.asList(signer, cc))
                .brandId(args.getBrandId())
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
    }
}
