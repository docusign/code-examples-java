package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.OrganizationsApi;
import com.docusign.admin.api.UsersApi;
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
 * This example demonstrates how to delete user data from organization.
 */
@Controller
@RequestMapping("/a010")
public class A010DeleteUserDataFromOrganization extends AbstractAdminController {

    public A010DeleteUserDataFromOrganization(DSConfiguration config, User user, Session session) {
        super(config, "a010", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();

        UUID organizationId = this.getOrganizationId(accessToken, basePath);
        UsersApi usersApi = createUsersApi(accessToken, basePath);
        OrganizationsApi organizationsApi = createOrganizationsApi(accessToken, basePath);

        IndividualUserDataRedactionResponse individualUserDataRedactionResponse = (new UserDataManagementService()).deleteUserDataFromOrganizationByEmail(
                usersApi,
                organizationsApi,
                args.getEmail(),
                organizationId);

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(individualUserDataRedactionResponse)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
