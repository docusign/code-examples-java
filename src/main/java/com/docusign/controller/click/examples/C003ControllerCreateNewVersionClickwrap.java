package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.controller.click.services.CreateNewVersionClickwrapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a clickwrap.
 */
@Controller
@RequestMapping("/c003")
public class C003ControllerCreateNewVersionClickwrap extends AbstractClickController {

    private static final String MODEL_CLICKWRAP_OK = "clickwrapOk";
    private static final String DOCUMENT_FILE_NAME = "TermsOfService.pdf";
    private static final Integer DOCUMENT_ORDER = 0;
    private static final String DOCUMENT_NAME = "Terms of Service";
    private final Session session;
    private final User user;

    @Autowired
    public C003ControllerCreateNewVersionClickwrap(DSConfiguration config, Session session, User user) {
        super(config, "c003", "Create a new clickwrap version");
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
                            HttpServletResponse response) throws IOException, ApiException {
        // Step 2: Construct your API headers
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        ClickwrapVersionSummaryResponse createdClickwrap = CreateNewVersionClickwrapService.createNewVersionClickwrap(
                accountsApi,
                CreateNewVersionClickwrapService.createClickwrapRequest(DOCUMENT_FILE_NAME, DOCUMENT_NAME, DOCUMENT_ORDER),
                this.session.getAccountId(),
                this.session.getClickwrapId()
        );



        this.session.setClickwrapId(createdClickwrap.getClickwrapId());
        this.session.setClickwrapVersionNumber(createdClickwrap.getVersionNumber());
        DoneExample.createDefault(this.title)
                .withJsonObject(createdClickwrap)
                .withMessage("Version " + createdClickwrap.getVersionNumber() + " of clickwrap " + 
                createdClickwrap.getClickwrapName() + " has been created.")
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
