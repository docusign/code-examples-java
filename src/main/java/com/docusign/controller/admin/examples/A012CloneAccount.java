package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.ProvisionAssetGroupApi;
import com.docusign.admin.model.AssetGroupAccountsResponse;
import com.docusign.admin.model.AssetGroupAccountClone;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.CloneAccountService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * This example demonstrates how to clone the account.
 */
@Controller
@RequestMapping("/a012")
public class A012CloneAccount extends AbstractAdminController {

    public A012CloneAccount(DSConfiguration config, Session session, User user) {
       super(config, "a012", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        ProvisionAssetGroupApi provisionAssetGroupApi = this.createProvisionAssetGroupApi(user.getAccessToken(), session.getBasePath());

        AssetGroupAccountsResponse groupAccounts = (new CloneAccountService()).getGroupAccounts(provisionAssetGroupApi, organizationId);
        model.addAttribute("groups", groupAccounts.getAssetGroupAccounts());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();
        UUID organizationId = this.getOrganizationId(accessToken, basePath);
        ProvisionAssetGroupApi provisionAssetGroupApi = this.createProvisionAssetGroupApi(accessToken, basePath);

        AssetGroupAccountClone assetGroupAccountClone =(new CloneAccountService()).getClonedAccount(
            provisionAssetGroupApi,
            organizationId,
            args.getSourceAccountId(),
            args.getTargetAccountName(),
            args.getTargetAccountEmail(),
            args.getTargetAccountFirstName(),
            args.getTargetAccountLastName());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
            .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
            .withJsonObject(assetGroupAccountClone)
            .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
