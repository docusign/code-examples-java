package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.PermissionSetUserGroupsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.GroupsApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.ErrorDetails;
import com.docusign.esign.model.GroupInformation;
import com.docusign.esign.model.PermissionProfileInformation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


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

    public EG025ControllerPermissionSetUserGroups(DSConfiguration config, Session session, User user) {
        super(config, "eg025", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        String accountId = session.getAccountId();

        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());
        PermissionProfileInformation permissionsInfo = accountsApi.listPermissions(accountId);
        model.addAttribute(MODEL_LIST_PROFILES, permissionsInfo.getPermissionProfiles());

        //ds-snippet-start:eSign25Step4
        GroupsApi groupsApi = new GroupsApi(accountsApi.getApiClient());
        GroupInformation groupInformation = groupsApi.listGroups(accountId);
        model.addAttribute(MODEL_LIST_GROUPS, groupInformation.getGroups());
        //ds-snippet-end:eSign25Step4
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException {
        // Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());

        //ds-snippet-start:eSign25Step3
        GroupInformation newGroupInfo = PermissionSetUserGroupsService.permissionSetUserGroups(
                apiClient,
                args.getGroupId(),
                args.getProfileId(),
                session.getAccountId()
        );
        //ds-snippet-end:eSign25Step3

        // Show result
        ErrorDetails errorDetails = newGroupInfo.getGroups().get(0).getErrorDetails();
        if (errorDetails != null) {
            new DoneExample()
                    .withTitle(exampleName)
                    .withName(title)
                    .withMessage(
                            getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage
                                    + "< br />Reason:"
                                    + errorDetails.getMessage())
                    .addToModel(model, config);
            return ERROR_PAGE;
        }
        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withJsonObject(newGroupInfo)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
