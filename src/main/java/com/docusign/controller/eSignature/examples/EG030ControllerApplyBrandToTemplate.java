package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.ApplyBrandToTemplateService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BrandsResponse;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.TemplateRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


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

    public EG030ControllerApplyBrandToTemplate(DSConfiguration config, Session session, User user) {
        super(config, "eg030", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        String accountId = session.getAccountId();
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        AccountsApi accountsApi = new AccountsApi(apiClient);
        BrandsResponse brands = accountsApi.listBrands(accountId);
        model.addAttribute(MODEL_LIST_BRAND, brands.getBrands());

        model.addAttribute(MODEL_TEMPLATE_OK, StringUtils.isNotBlank(session.getTemplateId()));
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = makeEnvelope(args, session.getTemplateId());

        // Step 4: Call the eSignature REST API
        EnvelopeSummary envelopeSummary = ApplyBrandToTemplateService.applyBrandToTemplate(
                envelopesApi,
                session.getAccountId(),
                envelope
        );

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(envelopeSummary)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeSummary.getEnvelopeId())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }

    //ds-snippet-start:eSign30Step3
    private static EnvelopeDefinition makeEnvelope(WorkArguments args, String templateId) {
        TemplateRole signer = new TemplateRole()
                .email(args.getSignerEmail())
                .name(args.getSignerName())
                .roleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        TemplateRole cc = new TemplateRole()
                .email(args.getCcEmail())
                .name(args.getCcName())
                .roleName(EnvelopeHelpers.CC_ROLE_NAME);

        return new EnvelopeDefinition()
                .templateId(templateId)
                .templateRoles(Arrays.asList(signer, cc))
                .brandId(args.getBrandId())
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
    }
    //ds-snippet-end:eSign30Step3
}
