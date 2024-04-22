package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.PermissionCreateService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.PermissionProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Permission profiles are collections of account settings that determine the
 * behavior and actions available to the user groups to which they're applied.
 * This code example demonstrates how to create a permission profile with the
 * eSignature REST API.
 */
@Controller
@RequestMapping("/eg024")
public class EG024ControllerPermissionCreate extends AbstractEsignatureController {

    public EG024ControllerPermissionCreate(DSConfiguration config, Session session, User user) {
        super(config, "eg024", session, user);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {

        // Step 2. Construct your API headers
        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());

        // Step 4. Call the eSignature REST API
        PermissionProfile newProfile = PermissionCreateService.createNewProfile(
                accountsApi,
                session.getAccountId(),
                args.getPermissionProfileName()
        );

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(newProfile)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", newProfile.getPermissionProfileId())
                        .replaceFirst("\\{1}", newProfile.getPermissionProfileName())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
