package com.docusign.controller.admin.examples;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.DsGroupsApi;
import com.docusign.admin.api.ProductPermissionProfilesApi;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.AddUserResponse;
import com.docusign.admin.model.DSGroupListResponse;
import com.docusign.admin.model.DSGroupRequest;
import com.docusign.admin.model.NewMultiProductUserAddRequest;
import com.docusign.admin.model.ProductPermissionProfileRequest;
import com.docusign.admin.model.ProductPermissionProfileResponse;
import com.docusign.admin.model.ProductPermissionProfilesResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.CreateActiveCLMESignUserService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Create new user This example demonstrates how to create a new user with
 * methods from Admin API.
 */
@Controller
@RequestMapping("/a002")
public class A002CreateActiveCLMESignUser extends AbstractAdminController {

    private final User user;
    private final Session session;

    @Autowired
    public A002CreateActiveCLMESignUser(DSConfiguration config, Session session, User user) {

        super(config, "a002");
        this.user = user;
        this.session = session;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = this.getExistingAccountId(user.getAccessToken(), session.getBasePath(), organizationId);

        // Step 3 start
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
            // logger.info("ProductName = " + eachOne);
            if (eachOne.getProductName().equals("CLM")) {
                clmProductId = eachOne.getProductId();
                clmProfiles = eachOne;
            } else {
                eSignProductId = eachOne.getProductId();
                eSignProfiles = eachOne;
            }
        }
        // Step 3 end
        
        // Step 4 start
        DsGroupsApi groupsApi = this.createDSGroupsApi(user.getAccessToken(), session.getBasePath());
        DSGroupListResponse groups = groupsApi.getDSGroups(organizationId, accountId);
        // Step 4 end

        // set CLM permission profiles
        model.addAttribute("listCLM", clmProfiles);
        model.addAttribute("clmProductId", clmProductId);

        // set eSign permission profiles
        model.addAttribute("listeSign", eSignProfiles);
        model.addAttribute("eSignProductId", eSignProductId);

        model.addAttribute("listGroups", groups);
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
                .createDefault(this.codeExampleText.ExampleName)
                .withMessage(this.codeExampleText.ResultsPageText)
                .withJsonObject(result)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
