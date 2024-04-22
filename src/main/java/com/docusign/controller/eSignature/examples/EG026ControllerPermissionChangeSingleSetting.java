package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.services.PermissionChangeSingleSettingService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.PermissionProfile;
import com.docusign.esign.model.PermissionProfileInformation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This code example demonstrates how to edit individual permission settings on
 * a permission profile with the eSignature REST API.
 */
@Controller
@RequestMapping("/eg026")
public class EG026ControllerPermissionChangeSingleSetting extends AbstractEsignatureController {

    private static final String MODEL_CUR_PROFILE_NAME = "permissionProfileName";

    private static final String MODEL_CUR_PROFILE_ID = "profileId";

    private static final String MODEL_LIST_PROFILES = "listProfiles";

    public EG026ControllerPermissionChangeSingleSetting(DSConfiguration config, Session session, User user) {
        super(config, "eg026", session, user);
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

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", newProfile.getPermissionProfileId())
                )
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE_COMPARE;
    }
}
