package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.NewUserResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.AddActiveUserService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.GroupsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.GroupInformation;
import com.docusign.esign.model.PermissionProfileInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.UUID;

/**
 * Create new user
 * This example demonstrates how to create a new user with methods from Admin API.
 */
@Controller
@RequestMapping("/a001")
public class A001AddActiveUser extends AbstractAdminController {

    private static final String MODEL_LIST_PROFILES = "listProfiles";
    private static final String MODEL_LIST_GROUPS = "listGroups";
    @Autowired
    public A001AddActiveUser(DSConfiguration config, Session session, User user) {
        super(config, "a001", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {

        try {
            super.onInitModel(args, model);
            System.out.println("super called");
            //ds-snippet-start:Admin1Step2
            ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
            apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + user.getAccessToken());
            //ds-snippet-end:Admin1Step2
            System.out.println("API client called");

            //ds-snippet-start:Admin1Step3
            AccountsApi accountsApi = new AccountsApi(apiClient);
            System.out.println(this.user.getAccessToken());
            System.out.println(this.session.getBasePath());

            UUID orgId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
            System.out.println("we have an org id: "+ orgId);
            UUID accountId = this.getExistingAccountId(this.user.getAccessToken(), this.session.getBasePath(), orgId);
            System.out.println("An accountID: "+ accountId);
            PermissionProfileInformation permissionsInfo = accountsApi.listPermissions(String.valueOf(accountId));
            //ds-snippet-end:Admin1Step3
            System.out.println("and permissions info too: " + permissionsInfo);
            model.addAttribute(MODEL_LIST_PROFILES, permissionsInfo.getPermissionProfiles());

            //ds-snippet-start:Admin1Step4
            GroupsApi groupsApi = new GroupsApi(apiClient);
            GroupInformation groupInformation = groupsApi.listGroups(String.valueOf(accountId));
            //ds-snippet-end:Admin1Step4

            model.addAttribute(MODEL_LIST_GROUPS, groupInformation.getGroups());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        System.out.println("doWork function called");
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();
        // Create a users api instance
        UsersApi usersApi = createUsersApi(accessToken, basePath);

        // Collect ids needed for the request
        UUID organizationId = this.getOrganizationId(accessToken, basePath);

        //ds-snippet-start:Admin1Step5
        UUID accountId = this.getExistingAccountId(accessToken, basePath, organizationId);
        //ds-snippet-end:Admin1Step5
        NewUserResponse result = AddActiveUserService.createNewActiveUser(
                args.getGroupId(),
                args.getProfileId(),
                args.getEmail(),
                args.getUserName(),
                args.getFirstName(),
                args.getLastName(),
                usersApi,
                organizationId,
                accountId);

        // Process results
        DoneExample
                .createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(result)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
