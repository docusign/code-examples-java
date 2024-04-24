package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.DsGroupsApi;
import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.client.ApiException;
import com.docusign.admin.model.AddUserResponse;
import com.docusign.admin.model.DSGroupListResponse;
import com.docusign.admin.model.ProductPermissionProfileResponse;
import com.docusign.admin.model.ProductPermissionProfilesResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.CreateActiveCLMESignUserService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Create new user This example demonstrates how to create a new user with
 * methods from Admin API.
 */
@Controller
@RequestMapping("/a002")
public class A002CreateActiveCLMESignUser extends AbstractAdminController {

    public A002CreateActiveCLMESignUser(DSConfiguration config, Session session, User user) {
        super(config, "a002", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = this.getExistingAccountId(user.getAccessToken(), session.getBasePath(), organizationId);

        //ds-snippet-start:Admin2Step3
        ProductPermissionProfilesApi permissionsInfo = this.createProductPermissionProfilesApi(user.getAccessToken(),
                session.getBasePath());
        ProductPermissionProfilesResponse profiles = permissionsInfo.getProductPermissionProfiles(organizationId,
                accountId);
        ProductPermissionProfileResponse clmProfiles = null;
        ProductPermissionProfileResponse eSignProfiles = null;
        UUID clmProductId = null;
        UUID eSignProductId = null;
        // filter by each type per response
        for (ProductPermissionProfileResponse eachOne : profiles.getProductPermissionProfiles()) {
            if (eachOne.getProductName().equals("CLM")) {
                clmProductId = eachOne.getProductId();
                clmProfiles = eachOne;
            } else {
                eSignProductId = eachOne.getProductId();
                eSignProfiles = eachOne;
            }
        }
        //ds-snippet-end:Admin2Step3

        //ds-snippet-start:Admin2Step4
        DsGroupsApi groupsApi = this.createDSGroupsApi(user.getAccessToken(), session.getBasePath());
        DSGroupListResponse groups = groupsApi.getDSGroups(organizationId, accountId);
        //ds-snippet-end:Admin2Step4

        // set CLM permission profiles
        model.addAttribute("listCLM", clmProfiles);
        model.addAttribute("clmProductId", clmProductId);

        // set eSign permission profiles
        model.addAttribute("listeSign", eSignProfiles);
        model.addAttribute("eSignProductId", eSignProductId);

        model.addAttribute("listGroups", groups);

        if (groups.getTotalCount() == 0) {
            throw new ApiException(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage);
        }
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();
        // Create a users api instance
        UsersApi usersApi = createUsersApi(accessToken, basePath);

        // Collect ids needed for the request
        UUID organizationId = this.getOrganizationId(accessToken, basePath);
        UUID accountId = this.getExistingAccountId(accessToken, basePath, organizationId);

        AddUserResponse result = CreateActiveCLMESignUserService.createNewActiveUser(
                args.getClmProfileId(),
                args.getClmProductId(),
                args.getESignProfileId(),
                args.getESignProductId(),
                args.getDsGroupId(),
                args.getUserName(),
                args.getFirstName(),
                args.getLastName(),
                args.getEmail(),
                usersApi,
                organizationId,
                accountId);

        this.session.setEmailAddress(result.getEmail());

        // Process results
        DoneExample
                .createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(result)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
