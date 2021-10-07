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

        super(config, "a002", "Create new active user for CLM and eSignature");
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
        AddUserResponse result = createNewActiveUser(this.user.getAccessToken(), args);

        // Process results
        DoneExample.createDefault(title)
        .withMessage("Results from MultiProductUserManagement:addOrUpdateUser method:")
        .withJsonObject(result)
        .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    protected AddUserResponse createNewActiveUser(String accessToken, WorkArguments args) throws Exception {
        // Create a users api instance
        UsersApi usersApi = createUsersApi(accessToken, this.session.getBasePath());

        // Collect ids needed for the request
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = this.getExistingAccountId(accessToken, this.session.getBasePath(), organizationId);


        // Step 5 start
        ProductPermissionProfileRequest clm = new ProductPermissionProfileRequest();
        ProductPermissionProfileRequest eSign = new ProductPermissionProfileRequest();
        clm.setPermissionProfileId(args.getClmProfileId());
        clm.setProductId(args.getClmProductId());

        eSign.setPermissionProfileId(args.getESignProfileId());
        eSign.setProductId(args.getESignProductId());

        DSGroupRequest dsGroup = new DSGroupRequest();
        dsGroup.setDsGroupId(args.getDsGroupId());
        // Fill the request with data from the form

        NewMultiProductUserAddRequest accountAddRequest = new NewMultiProductUserAddRequest()
                .defaultAccountId(accountId)
                .addProductPermissionProfilesItem(clm)
                .addProductPermissionProfilesItem(eSign)
                .addDsGroupsItem(dsGroup)
                .userName(args.getUserName())
                .firstName(args.getFirstName())
                .lastName(args.getLastName())
                .autoActivateMemberships(true)
                .email(args.getEmail());
        // Step 5 end

        // Step 6 start
        return usersApi.addOrUpdateUser(organizationId, accountId, accountAddRequest);
        // Step 6 end
    }
}
