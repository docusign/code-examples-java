package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.click.model.DisplaySettings;
import com.docusign.click.model.Document;
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
        AccountsApi accountsApi = this.createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3: Construct the request body for your clickwrap
        ClickwrapRequest clickwrapRequest = this.createClickwrapRequest();

        // Step 4: Call the v1 Click API
        ClickwrapVersionSummaryResponse createdClickwrap = accountsApi.createClickwrapVersion(
                this.session.getAccountId(),
                this.session.getClickwrapId(),
                clickwrapRequest);

        this.session.setClickwrapId(createdClickwrap.getClickwrapId());
        this.session.setClickwrapVersionNumber(createdClickwrap.getVersionNumber().toString());
        DoneExample.createDefault(this.title)
                .withJsonObject(createdClickwrap)
                .withMessage("The new clickwrap version has been created!<br />"
                        + "Clickwrap ID " + createdClickwrap.getClickwrapId()
                        + ".<br /> Version number " + createdClickwrap.getVersionNumber())
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }

    private ClickwrapRequest createClickwrapRequest() throws IOException {
        Document document = ClickwrapHelper.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, DOCUMENT_ORDER);
        DisplaySettings displaySettings = new DisplaySettings()
                .displayName("Terms of Service v2")
                .consentButtonText("I Agree")
                .downloadable(true)
                .format("modal")
                .mustRead(true)
                .mustView(true)
                .requireAccept(true)
                .documentDisplay("document");

        return new ClickwrapRequest()
                .addDocumentsItem(document)
                .clickwrapName("Terms of Service")
                .requireReacceptance(true)
                .status(ClickwrapHelper.STATUS_ACTIVE)
                .displaySettings(displaySettings);
    }
    // ***DS.snippet.0.end
}
