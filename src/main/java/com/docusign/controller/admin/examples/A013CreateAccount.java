package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.ProvisionAssetGroupApi;
import com.docusign.admin.model.OrganizationSubscriptionResponse;
import com.docusign.admin.model.SubscriptionProvisionModelAssetGroupWorkResult;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.CreateAccountService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * This example demonstrates how to create the account.
 */
@Controller
@RequestMapping("/a013")
public class A013CreateAccount extends AbstractAdminController {

    public A013CreateAccount(DSConfiguration config, Session session, User user) {
        super(config, "a013", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();
        UUID organizationId = this.getOrganizationId(accessToken, basePath);

        ProvisionAssetGroupApi provisionAssetGroupApi = this.createProvisionAssetGroupApi(accessToken, basePath);

        OrganizationSubscriptionResponse planItems = CreateAccountService.getFirstPlanItem(
                provisionAssetGroupApi,
                organizationId);

        SubscriptionProvisionModelAssetGroupWorkResult createdAccount = CreateAccountService
                .createAccountBySubscription(
                        provisionAssetGroupApi,
                        organizationId,
                        args.getEmail(),
                        args.getFirstName(),
                        args.getLastName(),
                        planItems.getSubscriptionId(),
                        planItems.getPlanId());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(createdAccount)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
