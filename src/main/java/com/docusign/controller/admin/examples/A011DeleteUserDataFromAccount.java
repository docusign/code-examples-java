package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.AccountsApi;
import com.docusign.admin.model.IndividualUserDataRedactionResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.UserDataManagementService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * This example demonstrates how to delete user data from account.
 */
@Controller
@RequestMapping("/a011")
public class A011DeleteUserDataFromAccount extends AbstractAdminController {

    public A011DeleteUserDataFromAccount(DSConfiguration config, Session session, User user) {
        super(config, "a011", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();

        UUID organizationId = this.getOrganizationId(accessToken, basePath);
        UUID existingAccountId = this.getExistingAccountId(accessToken, basePath, organizationId);
        AccountsApi accountsApi = createAccountsApi(accessToken, basePath);

        IndividualUserDataRedactionResponse userDataRedactionResponse = (new UserDataManagementService()).deleteUserDataFromAccountByUserId(
                accountsApi,
                args.getUserId(),
                existingAccountId);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(userDataRedactionResponse)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
