package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.model.OrganizationImportResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.BulkImportUserDataService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


/**
 * How to add users via bulk import
 * This example demonstrates how to add users via bulk import from the Admin API.
 */
@Controller
@RequestMapping("/a004")
public class A004BulkImportUserData extends AbstractAdminController {

    private final Session session;
    private final User user;

    @Autowired
    public A004BulkImportUserData(DSConfiguration config, Session session, User user) {
        super(config, "a004");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {


        OrganizationImportResponse result = bulkImportUserData(this.user.getAccessToken());

        this.session.setImportId(result.getId().toString());
        
        // Process results
        DoneExample
                .createDefault(getTextForCodeExample().ExampleName)
                .withMessage(getTextForCodeExample().ResultsPageText)
                .withJsonObject(result)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }

    protected OrganizationImportResponse bulkImportUserData(String accessToken) throws Exception {
        // Collect ids and user data needed for the request
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = this.getExistingAccountId(accessToken, this.session.getBasePath(), organizationId);
        // Make sure you're using a verified domain for auto-activation to work properly
        // Step 3 start
        BulkImportsApi bulkImportsApi = createBulkImportsApi(accessToken, this.session.getBasePath());
        
        return BulkImportUserDataService.bulkImportUserData(bulkImportsApi, organizationId, accountId);
        // Step 3 end
    }
}
