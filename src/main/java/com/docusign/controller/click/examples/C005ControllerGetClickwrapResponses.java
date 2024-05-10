package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapAgreementsResponse;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.click.services.GetClickwrapResponsesService;
import com.docusign.controller.click.services.GetListClickwrapsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Get clickwrap responses.
 */
@Controller
@RequestMapping("/c005")
public class C005ControllerGetClickwrapResponses extends AbstractClickController {

    private static final String MODEL_CLICKWRAPS = "clickwraps";

    private final Session session;

    private final User user;

    public C005ControllerGetClickwrapResponses(DSConfiguration config, Session session, User user) {
        super(config, "c005");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        ClickwrapVersionsResponse clickwraps = GetListClickwrapsService.getListClickwrap(accountsApi, this.session.getAccountId());
        model.addAttribute(MODEL_CLICKWRAPS, clickwraps);
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException {
        // Step 2: Construct your API headers
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3: Call the v1 Click API
        //ds-snippet-start:Click5Step3
        ClickwrapAgreementsResponse clickwrapAgreementsResponse = GetClickwrapResponsesService.getClickwrapResponses(
                accountsApi,
                this.session.getAccountId(),
                args.getClickwrapId());
        //ds-snippet-end:Click5Step3

        DoneExample.createDefault(this.title)
                .withJsonObject(clickwrapAgreementsResponse)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
