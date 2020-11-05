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
        findProfile(profiles, session.getPermissionProfileId()).ifPresent((PermissionProfile curProfile) -> {
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

        // Step 3. Construct your request body
        PermissionProfileInformation permissionsInfo = accountsApi.listPermissions(accountId);
        PermissionProfile profile = findProfile(permissionsInfo.getPermissionProfiles(), curProfileId)
                .orElseThrow(NoSuchElementException::new);

        // Step 4. Call the eSignature REST API
        AccountRoleSettings newSettings = Objects.requireNonNullElse(profile.getSettings(),
                DsModelUtils.createDefaultRoleSettings());
        profile.setSettings(changeRandomSettings(newSettings));
        PermissionProfile newProfile = accountsApi.updatePermissionProfile(accountId, curProfileId, profile);

        DoneExample.createDefault(title).withMessage("The permission profile is updated!<br />Permission profile ID: "
                + newProfile.getPermissionProfileId() + ".").addToModel(model);
        return DONE_EXAMPLE_PAGE_COMPARE;
    }

    private static Optional<PermissionProfile> findProfile(List<PermissionProfile> profiles, String profileId) {

        return profiles.stream().filter(p -> p.getPermissionProfileId().equals(profileId)).findFirst();
    }

    // Changes random boolean properties; in a real application, changing properties
    // will be read from the page or in a different way
    private static AccountRoleSettings changeRandomSettings(AccountRoleSettings settings) {
        Gson gson = new Gson();
        // Change this value back to: gson.fromJson(gson.toJson(settings),
        // AccountRoleSettings.class);
        // as soon as the signinguiversion is added back to the swagger spec.
        // Also change the type back from AccountRoleSettingsPatch to AccountRoleSettings
        AccountRoleSettingsPatch newSettings = gson.fromJson(gson.toJson(settings), AccountRoleSettingsPatch.class);
        newSettings.signingUiVersion("1");
        Random random = new Random(System.currentTimeMillis());

        return newSettings.canCreateWorkspaces(randomBool(random)).allowEnvelopeSending(randomBool(random))
                .allowSignerAttachments(randomBool(random)).allowESealRecipients(randomBool(random))
                .allowTaggingInSendAndCorrect(randomBool(random)).allowWetSigningOverride(randomBool(random))
                .enableApiRequestLogging(randomBool(random)).enableRecipientViewingNotifications(randomBool(random))
                .allowSupplementalDocuments(randomBool(random)).disableDocumentUpload(randomBool(random));
    }

    private static String randomBool(Random random) {
        if (random.nextBoolean()) {
            return DsModelUtils.TRUE;
        }
        return DsModelUtils.FALSE;
    }
}
