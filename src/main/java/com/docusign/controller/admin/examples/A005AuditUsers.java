package com.docusign.controller.admin.examples;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.api.UsersApi.GetUserProfilesOptions;
import com.docusign.admin.api.UsersApi.GetUsersOptions;
import com.docusign.admin.model.OrganizationUserResponse;
import com.docusign.admin.model.OrganizationUsersResponse;
import com.docusign.admin.model.UserDrilldownResponse;
import com.docusign.admin.model.UsersDrilldownResponse;
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
@RequestMapping("/a005")
public class A005AuditUsers extends AbstractAdminController {

    private final User user;
    private final Session session;

    @Autowired
    public A005AuditUsers(DSConfiguration config, Session session, User user) {
        super(config, "a005", "Audit Users");
        this.user = user;
        this.session = session;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        ArrayList<UserDrilldownResponse> resultsArr = new ArrayList<UserDrilldownResponse>();


        // Collect ids needed for the request
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = this.getExistingAccountId(this.user.getAccessToken(), this.session.getBasePath(), organizationId);

        UsersApi adminApi = createUsersApi(this.user.getAccessToken(), this.session.getBasePath());

        // Step 3 start
        GetUsersOptions options = adminApi.new GetUsersOptions();
        options.setAccountId(accountId);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 10)));
        options.setLastModifiedSince(nowAsISO);  
        OrganizationUsersResponse modifiedUsers = adminApi.getUsers(organizationId, options);
        // Step 3 end

        // Step 5 start
for (OrganizationUserResponse user : modifiedUsers.getUsers()) {
    GetUserProfilesOptions profilesOptions = adminApi.new GetUserProfilesOptions();
    profilesOptions.setEmail(user.getEmail());
    UsersDrilldownResponse res = adminApi.getUserProfiles(organizationId, profilesOptions);
    resultsArr.add(res.getUsers().get(0));
}
        // Step 5 end

        // Process results
        DoneExample.createDefault(title).withMessage("Results from eSignUserManagement:getUserProfiles method:")
                .withJsonObject(resultsArr).addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

}
