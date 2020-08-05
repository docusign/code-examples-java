package com.docusign.controller.examples;
import java.util.Objects;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.docusign.model.AccountRoleSettingsPatch;
import com.docusign.DSConfiguration;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountRoleSettings;
import com.docusign.esign.model.PermissionProfile;
import com.docusign.model.DoneExample;
import com.docusign.model.Session;
import com.docusign.model.User;
import com.google.gson.Gson;
/**
 * Permission profiles are collections of account settings that determine the
 * behavior and actions available to the user groups to which they're applied.
 * This code example demonstrates how to create a permission profile with the
 * eSignature REST API.
 */
@Controller
@RequestMapping("/eg024")
public class EG024ControllerPermissionCreate extends AbstractController{

    private final Session session;
    private final User user;

    @Autowired
    public EG024ControllerPermissionCreate(DSConfiguration config, Session session, User user) {
        super(config, "eg024", "Creates a new permission profile");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        
        // Step 2. Construct your API headers
        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());

        // Step 3. Construct your request body
        // We're Overloading the AccountSettings class with AccountSettingsPatch to include the signingUIVersion which is missing in the swagger spec at this time.
        Gson gson = new Gson();
        AccountRoleSettings settings = DsModelUtils.createDefaultRoleSettings();
        AccountRoleSettings newSettings = gson.fromJson(gson.toJson(settings), AccountRoleSettingsPatch.class);
        ((AccountRoleSettingsPatch) newSettings).signingUiVersion("1");
        PermissionProfile profile = new PermissionProfile()
                .permissionProfileName(args.getPermissionProfileName())
                .settings(newSettings);
                
        // Step 4. Call the eSignature REST API
        PermissionProfile newProfile = accountsApi.createPermissionProfile(session.getAccountId(), profile);

        DoneExample.createDefault(title)
                .withJsonObject(newProfile)
                .withMessage("The permission profile is created!<br />Permission profile ID: "
                    + newProfile.getPermissionProfileId() + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}