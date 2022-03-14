package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.controller.click.services.ActivateClickwrapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        ClickwrapVersionSummaryResponse updatedClickwrap = ActivateClickwrapService.activateClickwrap(
                accountsApi,
                this.session.getAccountId(),
                this.session.getClickwrapId(),
                this.session.getClickwrapVersionNumber());

        DoneExample.createDefault(this.title)
                .withJsonObject(updatedClickwrap)
                .withMessage("The clickwrap " + updatedClickwrap.getClickwrapName() + " has been activated.")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
