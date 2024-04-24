package com.docusign.controller.click.examples;

import com.docusign.DSConfiguration;
import com.docusign.click.api.AccountsApi;
import com.docusign.click.client.ApiException;
import com.docusign.click.model.ClickwrapVersionsResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.click.services.ActivateClickwrapService;
import com.docusign.controller.click.services.EmbedClickwrapService;
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
@RequestMapping("/c006")
public class C006ControllerEmbedClickwrap extends AbstractClickController {

    private static final String MODEL_CLICKWRAPS = "clickwraps";

    private final Session session;

    private final User user;

    public C006ControllerEmbedClickwrap(DSConfiguration config, Session session, User user) {
        super(config, "c006");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());
        ClickwrapVersionsResponse clickwraps = EmbedClickwrapService.getActiveClickwraps(accountsApi, this.session.getAccountId());
        ClickwrapVersionsResponse inactiveClickwraps = ActivateClickwrapService.getClickwrapsByStatus(accountsApi, this.session.getAccountId(), "inactive");
        Boolean hasInactiveClickraps = inactiveClickwraps.getClickwraps().size() > 0;
        model.addAttribute(MODEL_CLICKWRAPS, clickwraps);
        model.addAttribute("hasInactiveClickraps", hasInactiveClickraps);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException {
        // Step 2: Construct your API headers
        AccountsApi accountsApi = createAccountsApiClient(this.session.getBasePath(), this.user.getAccessToken());

        String clickwrapId = args.getClickwrap();
        String fullName = args.getFullName();
        String email = args.getEmail();
        String company = args.getCompany();
        String title = args.getTitle();
        String date = args.getDate();

        String url = EmbedClickwrapService.createAgreementUrl(
                accountsApi, this.session.getAccountId(),
                clickwrapId, fullName, email, company, title, date);

        if (url == "Already Agreed") {
            new DoneExample()
                    .withTitle(exampleName)
                    .withName(title)
                    .withMessage("The email address was already used to agree to this elastic template. Provide a different email address if you want to view the agreement and agree to it.")
                    .addToModel(model, config);
            return ERROR_PAGE;
        }
        else
        {
            String htmlSnippet = "<p id='agreementStatus'>NOT AGREED</p><div id='ds-terms-of-service'></div><script src='https://demo.docusign.net/clickapi/sdk/latest/docusign-click.js'></script><script>docuSignClick.Clickwrap.render({agreementUrl: '" + url + "',onAgreed: function () {document.getElementById('agreementStatus').innerHTML = 'AGREED';}}, '#ds-terms-of-service');</script>";
            
            DoneExample.createDefault(this.title)
                    .withJsonObject(url)
                    .withMessage(getTextForCodeExample().ResultsPageText + htmlSnippet)
                    .addToModel(model, config);
            return DONE_EXAMPLE_PAGE;
        }
    }
}
