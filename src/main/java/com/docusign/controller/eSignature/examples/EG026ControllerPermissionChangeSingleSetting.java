package com.docusign.controller.eSignature.examples;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import com.docusign.common.WorkArguments;
import com.docusign.core.model.AccountRoleSettingsPatch;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.services.eSignature.examples.PermissionChangeSingleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.docusign.DSConfiguration;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.AccountRoleSettings;
import com.docusign.esign.model.PermissionProfile;
import com.docusign.esign.model.PermissionProfileInformation;
import com.google.gson.Gson;

/**
 * This code example demonstrates how to edit individual permission settings on
 * a permission profile with the eSignature REST API.
 */
@Controller
@RequestMapping("/eg026")
public class EG026ControllerPermissionChangeSingleSetting extends AbstractEsignatureController {

    private static final String MODEL_PERMISSIONS = "permissions";
    private static final String MODEL_CUR_PROFILE_NAME = "permissionProfileName";
    private static final String MODEL_CUR_PROFILE_ID = "profileId";
    private static final String MODEL_LIST_PROFILES = "listProfiles";

    private final Session session;
    private final User user;

    @Autowired
    public EG026ControllerPermissionChangeSingleSetting(DSConfiguration config, Session session, User user) {
        super(config, "eg026", "Change single permission setting");
        this.session = session;
        this.user = user;
    }

    @GetMapping("/profile")
    public void getProfile(@RequestParam String profileId, ModelMap model) {
        session.setPermissionProfileId(profileId);
        model.addAttribute(MODEL_CUR_PROFILE_NAME, profileId);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());

        PermissionProfileInformation permissionsInfo = accountsApi.listPermissions(session.getAccountId());
        List<PermissionProfile> profiles = permissionsInfo.getPermissionProfiles();
        model.addAttribute(MODEL_LIST_PROFILES, profiles);

        model.addAttribute(MODEL_CUR_PROFILE_NAME, profiles.get(0).getPermissionProfileName());
        model.addAttribute(MODEL_CUR_PROFILE_ID, profiles.get(0).getPermissionProfileId());
        PermissionChangeSingleSettingService.findProfile(
                profiles,
                session.getPermissionProfileId()).ifPresent((PermissionProfile curProfile) ->
        {
            model.addAttribute(MODEL_CUR_PROFILE_NAME, curProfile.getPermissionProfileName());
            model.addAttribute(MODEL_CUR_PROFILE_ID, curProfile.getPermissionProfileId());
            // model.addAttribute(MODEL_PERMISSIONS,
            // Utils.compareFields(curProfile.getSettings(), null, true));
        });
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response)
            throws ApiException, IOException {
        // Step 2. Construct your API headers
        AccountsApi accountsApi = createAccountsApi(session.getBasePath(), user.getAccessToken());
        String accountId = session.getAccountId();
        String curProfileId = args.getProfileId();

        PermissionProfile newProfile = PermissionChangeSingleSettingService.permissionChangeSingleSetting(
                accountsApi,
                accountId,
                curProfileId
        );

        DoneExample.createDefault(title).withMessage("The permission profile is updated!<br />Permission profile ID: "
                + newProfile.getPermissionProfileId() + ".").addToModel(model);
        return DONE_EXAMPLE_PAGE_COMPARE;
    }


}
