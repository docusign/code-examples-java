package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.click.services.ActivateClickwrapService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Activate a clickwrap.
 */
@Controller
@RequestMapping("/c002")
public class C002ControllerActivateClickwrap extends AbstractClickController {

    private static final String MODEL_CLICKWRAPS = "clickwraps";

    private final Session session;

    private final User user;

    public C002ControllerActivateClickwrap(DSConfiguration config, Session session, User user) {
        super(config, "c002");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        ClickwrapVersionsResponse inactiveClickwraps = ActivateClickwrapService.getClickwrapsByStatus(
                accountsApi,
                this.session.getAccountId(),
                "inactive");
        ClickwrapVersionsResponse draftClickwraps = ActivateClickwrapService.getClickwrapsByStatus(
                accountsApi,
                this.session.getAccountId(),
                "draft");
        inactiveClickwraps.getClickwraps().addAll(draftClickwraps.getClickwraps());
        model.addAttribute(MODEL_CLICKWRAPS, inactiveClickwraps);
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException {
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        String clickwrap = args.getClickwrap();
        String[] clickwrapData = clickwrap.split(":");
        String clickwrapId = clickwrapData[0];
        String versionNumber = clickwrapData[1];

        ClickwrapVersionSummaryResponse updatedClickwrap = ActivateClickwrapService.activateClickwrap(
                accountsApi,
                this.session.getAccountId(),
                clickwrapId,
                versionNumber);

        DoneExample.createDefault(this.title)
                .withJsonObject(updatedClickwrap)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
