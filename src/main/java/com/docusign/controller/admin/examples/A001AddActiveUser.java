package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.NewUserResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.GroupsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.model.GroupInformation;
import com.docusign.esign.model.PermissionProfileInformation;
import com.docusign.controller.admin.services.AddActiveUserService;
import com.docusign.controller.admin.services.GetExistingAccountIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
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
    private final User user;
    private final Session session;
    private final String accessToken;
    private final String basePath;
    private final UUID organizationId;
    private final UUID accountId;

    @Autowired
    public A001AddActiveUser(DSConfiguration config, Session session, User user) throws Exception {
        super(config, "a001", "Create a new active eSignature user");
        this.user = user;
        this.session = session;
        this.accessToken = this.user.getAccessToken();
        this.basePath = this.session.getBasePath();
        this.organizationId = this.getOrganizationId(accessToken, basePath);
        this.accountId = GetExistingAccountIdService.getExistingAccountId(
                createUsersApi(accessToken, basePath),
                config.getSignerEmail(),
                organizationId);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + user.getAccessToken());
        
        // Step 3 start
        AccountsApi accountsApi = new AccountsApi(apiClient);
        PermissionProfileInformation permissionsInfo = accountsApi.listPermissions(String.valueOf(accountId));
        // Step 3 end

        model.addAttribute(MODEL_LIST_PROFILES, permissionsInfo.getPermissionProfiles());

        // Step 4 start
        GroupsApi groupsApi = new GroupsApi(apiClient);
        GroupInformation groupInformation = groupsApi.listGroups(String.valueOf(accountId));
        // Step 4 end

        model.addAttribute(MODEL_LIST_GROUPS, groupInformation.getGroups());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        NewUserResponse result = AddActiveUserService.addActiveUser(
                args.getGroupId(),
                args.getProfileId(),
                args.getEmail(),
                args.getUserName(),
                args.getFirstName(),
                args.getLastName(),
                createUsersApi(accessToken, basePath),
                organizationId,
                accountId);

        // Process results
        DoneExample.createDefault(title)
                .withMessage("Results from eSignUserManagement:createUser method:")
                .withJsonObject(result)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
