package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.ApplyBrandToEnvelopeService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BrandsResponse;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
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
        // Construct your API headers
        //ds-snippet-start:eSign29Step2
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());
        //ds-snippet-end:eSign29Step2

        // Construct your envelope JSON body
        //ds-snippet-start:eSign29Step3
        EnvelopeDefinition envelope = ApplyBrandToEnvelopeService.makeEnvelope(
                args.getSignerEmail(),
                args.getSignerName(),
                args.getBrandId()
        );
        //ds-snippet-end:eSign29Step3

        // Call the eSignature REST API
        //ds-snippet-start:eSign29Step4
        EnvelopeSummary envelopeSummary = ApplyBrandToEnvelopeService.applyBrandToEnvelope(
                envelopesApi,
                session.getAccountId(),
                envelope
        );
        //ds-snippet-end:eSign29Step4

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(envelopeSummary)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeSummary.getEnvelopeId())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
