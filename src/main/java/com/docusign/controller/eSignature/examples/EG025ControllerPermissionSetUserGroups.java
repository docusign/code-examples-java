package com.docusign.controller.eSignature.examples;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.docusign.DSConfiguration;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.GroupsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.ErrorDetails;
import com.docusign.esign.model.Group;
import com.docusign.esign.model.GroupInformation;
import com.docusign.esign.model.PermissionProfileInformation;


/**
 * Permission profiles are collections of account settings that determine the
 * behavior and actions available to the user groups to which they're applied.
 * This code example demonstrates how to the permission profile for the group
 * with the eSignature REST API.
 */
@Controller
@RequestMapping("/eg025")
public class EG025ControllerPermissionSetUserGroups extends AbstractEsignatureController {

    private static final String MODEL_LIST_PROFILES = "listProfiles";
    private static final String MODEL_LIST_GROUPS = "listGroups";

    private final Session session;
    private final User user;


    @Autowired
    public EG025ControllerPermissionSetUserGroups(DSConfiguration config, Session session, User user) {
        super(config, "eg025", "Setting a permission profile");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        String accountId = session.getAccountId();

        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());
        PermissionProfileInformation permissionsInfo = accountsApi.listPermissions(accountId);
        model.addAttribute(MODEL_LIST_PROFILES, permissionsInfo.getPermissionProfiles());

        GroupsApi groupsApi = new GroupsApi(accountsApi.getApiClient());
        GroupInformation groupInformation = groupsApi.listGroups(accountId);
        model.addAttribute(MODEL_LIST_GROUPS, groupInformation.getGroups());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        // Step 2: Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        GroupsApi groupsApi = new GroupsApi(apiClient);

        // Step 3: Perform request
        Group newGroup = new Group()
                .groupId(args.getGroupId())
                .permissionProfileId(args.getProfileId());
        GroupInformation groupInformation = new GroupInformation()
                .groups(List.of(newGroup));
        GroupInformation newGroupInfo = groupsApi.updateGroups(session.getAccountId(), groupInformation);

        // Step 4: Show result
        ErrorDetails errorDetails = newGroupInfo.getGroups().get(0).getErrorDetails();
        if (errorDetails != null) {
            new DoneExample()
                    .withTitle(exampleName)
                    .withName(title)
                    .withMessage(errorDetails.getMessage())
                    .addToModel(model);
            return ERROR_PAGE;
        }
        DoneExample.createDefault(title)
                .withJsonObject(newGroupInfo)
                .withMessage("The permission profile was successfully set to the user group!")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
