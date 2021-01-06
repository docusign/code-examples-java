package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.*;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Activate a clickwrap.
 */
@Controller
@RequestMapping("/c002")
public class C002ControllerActivateClickwrap extends AbstractClickController {

    private static final String MODEL_CLICKWRAP_OK = "clickwrapOk";

    private final Session session;
    private final User user;

    @Autowired
    public C002ControllerActivateClickwrap(DSConfiguration config, Session session, User user) {
        super(config, "c002", "Activate a clickwrap");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_CLICKWRAP_OK, StringUtils.isNotBlank(this.session.getClickwrapId()));
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException {
        // Step 2: Construct your API headers
        AccountsApi accountsApi = this.createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3: Construct the request body for your clickwrap
        ClickwrapRequest clickwrapRequest = new ClickwrapRequest().status(ClickwrapHelper.STATUS_ACTIVE);

        // Step 4: Call the v1 Click API
        ClickwrapVersionSummaryResponse updatedClickwrap = accountsApi.updateClickwrapVersion(
                this.session.getAccountId(),
                this.session.getClickwrapId(),
                this.session.getClickwrapVersionNumber(),
                clickwrapRequest);

        DoneExample.createDefault(this.title)
                .withJsonObject(updatedClickwrap)
                .withMessage("The clickwrap has been activated!<br />Clickwrap ID " + updatedClickwrap.getClickwrapId() + ".")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
