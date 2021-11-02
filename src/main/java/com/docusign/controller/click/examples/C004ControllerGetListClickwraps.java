package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.controller.click.services.GetListClickwrapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Get a list of clickwraps.
 */
@Controller
@RequestMapping("/c004")
public class C004ControllerGetListClickwraps extends AbstractClickController {

    private final Session session;
    private final User user;

    @Autowired
    public C004ControllerGetListClickwraps(DSConfiguration config, Session session, User user) {
        super(config, "c004", "Get a list of clickwraps");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException {
        // Step 2: Construct your API headers
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        ClickwrapVersionsResponse clickwrapsResponse = GetListClickwrapsService.getListClickwrap(
                accountsApi,
                this.session.getAccountId());

        DoneExample.createDefault(this.title)
                .withJsonObject(clickwrapsResponse)
                .withMessage("Clickwraps have been returned!")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
