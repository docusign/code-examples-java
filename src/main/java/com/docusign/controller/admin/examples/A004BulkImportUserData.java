package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.model.OrganizationImportResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.BulkImportUserDataService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * How to add users via bulk import
 * This example demonstrates how to add users via bulk import from the Admin API.
 */
@Controller
@RequestMapping("/a004")
public class A004BulkImportUserData extends AbstractAdminController {

    public A004BulkImportUserData(DSConfiguration config, Session session, User user) {
        super(config, "a004", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        OrganizationImportResponse result = bulkImportUserData(this.user.getAccessToken());

        this.session.setImportId(result.getId().toString());

        // Process results
        DoneExample
                .createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(result)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }

    protected OrganizationImportResponse bulkImportUserData(String accessToken) throws Exception {
        // Collect ids and user data needed for the request
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = this.getExistingAccountId(accessToken, this.session.getBasePath(), organizationId);
        // Make sure you're using a verified domain for auto-activation to work properly
        //ds-snippet-start:Admin4Step3
        BulkImportsApi bulkImportsApi = createBulkImportsApi(accessToken, this.session.getBasePath());

        return BulkImportUserDataService.bulkImportUserData(bulkImportsApi, organizationId, accountId);
        //ds-snippet-end:Admin4Step3
    }
}
