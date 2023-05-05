package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BrandsResponse;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.controller.eSignature.services.ApplyBrandToEnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Applying a brand to an envelope<br />
 * The envelope includes a pdf document. Anchor text <i>AutoPlace</i> is used
 * to position the signing fields in the documents.
 */
@Controller
@RequestMapping("/eg029")
public class EG029ControllerApplyBrandToEnvelope extends AbstractEsignatureController {

    private static final String MODEL_LIST_BRAND = "listBrands";

    @Autowired
    public EG029ControllerApplyBrandToEnvelope(DSConfiguration config, Session session, User user) {
        super(config, "eg029", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());
        BrandsResponse brands = ApplyBrandToEnvelopeService.getBrands(accountsApi, session.getAccountId());
        model.addAttribute(MODEL_LIST_BRAND, brands.getBrands());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = ApplyBrandToEnvelopeService.makeEnvelope(
                args.getSignerEmail(),
                args.getSignerName(),
                args.getBrandId()
        );

        // Step 5: Call the eSignature REST API
        EnvelopeSummary envelopeSummary = ApplyBrandToEnvelopeService.applyBrandToEnvelope(
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
}
