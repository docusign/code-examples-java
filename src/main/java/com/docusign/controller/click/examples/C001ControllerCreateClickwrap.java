package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapRequest;
import com.docusign.click.model.ClickwrapVersionSummaryResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.click.services.CreateClickwrapService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create a clickwrap.
 */
@Controller
@RequestMapping("/c001")
public class C001ControllerCreateClickwrap extends AbstractClickController {

    private static final String DOCUMENT_FILE_NAME = "TermsOfService.pdf";

    private static final Integer DOCUMENT_ORDER = 0;

    private static final String DOCUMENT_NAME = "Terms of Service";

    private final Session session;

    private final User user;

    public C001ControllerCreateClickwrap(DSConfiguration config, Session session, User user) {
        super(config, "c001");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        // Step 2. Construct your API headers
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3. Construct the request body for your clickwrap
        //ds-snippet-start:Click1Step3
        ClickwrapRequest clickwrapRequest = CreateClickwrapService.createClickwrapRequest(
                args.getClickwrapName(),
                DOCUMENT_FILE_NAME,
                DOCUMENT_NAME,
                DOCUMENT_ORDER);
        //ds-snippet-end:Click1Step3

        // Step 4. Call the v1 Click API
        //ds-snippet-start:Click1Step4
        ClickwrapVersionSummaryResponse createdClickwrap = CreateClickwrapService.createClickwrap(
                accountsApi,
                this.session.getAccountId(),
                clickwrapRequest);
        //ds-snippet-end:Click1Step4

        this.session.setClickwrapId(createdClickwrap.getClickwrapId());
        this.session.setClickwrapVersionNumber(createdClickwrap.getVersionNumber());
        DoneExample.createDefault(this.title)
                .withJsonObject(createdClickwrap)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText.replaceFirst("\\{0}", createdClickwrap.getClickwrapName()))
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
    // ***DS.snippet.0.end
}
