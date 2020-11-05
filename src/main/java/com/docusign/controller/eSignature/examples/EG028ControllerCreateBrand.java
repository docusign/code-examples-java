package com.docusign.controller.eSignature.examples;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.docusign.common.WorkArguments;
import com.docusign.core.common.Languages;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docusign.DSConfiguration;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Brand;
import com.docusign.esign.model.BrandsResponse;


/**
 * Creating a brand. <br />
 * This code example demonstrates how to create a brand.
 */
@Controller
@RequestMapping("/eg028")
public class EG028ControllerCreateBrand extends AbstractEsignatureController{

    private static final String MODEL_LIST_LANGUAGE = "listLanguage";

    private final Session session;
    private final User user;


    @Autowired
    public EG028ControllerCreateBrand(DSConfiguration config, Session session, User user) {
        super(config, "eg028", "Creating a brand");
        this.session = session;
        this.user = user;
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
        String language = args.getLanguage();
        Brand brand = new Brand()
                .brandName(args.getBrandName())
                .defaultBrandLanguage(language)
                .brandLanguages(List.of(language));

        // Step 5: Call the eSignature REST API
        BrandsResponse brandsResponse = accountsApi.createBrand(session.getAccountId(), brand);

        DoneExample.createDefault(title)
                .withJsonObject(brandsResponse)
                .withMessage("The brand has been created!<br />Brand ID "
                    + brandsResponse.getBrands().get(0).getBrandId() + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
