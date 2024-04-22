package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.CreateBrandService;
import com.docusign.core.common.Languages;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.BrandsResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Creating a brand. <br />
 * This code example demonstrates how to create a brand.
 */
@Controller
@RequestMapping("/eg028")
public class EG028ControllerCreateBrand extends AbstractEsignatureController {

    private static final String MODEL_LIST_LANGUAGE = "listLanguage";

    public EG028ControllerCreateBrand(DSConfiguration config, Session session, User user) {
        super(config, "eg028", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_LIST_LANGUAGE, Languages.getSupportedLanguages());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        // Step 2: Construct your API headers
        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());

        // Step 3: Construct your brand JSON body
        //ds-snippet-start:eSign28Step3
        String language = args.getLanguage();

        BrandsResponse brandsResponse = CreateBrandService.createBrand(
                accountsApi,
                args.getBrandName(),
                language,
                session.getAccountId()
        );
        //ds-snippet-end:eSign28Step3

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(brandsResponse)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", brandsResponse.getBrands().get(0).getBrandId())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
