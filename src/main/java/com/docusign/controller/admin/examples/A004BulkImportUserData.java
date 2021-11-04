package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.model.OrganizationImportResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.controller.admin.services.BulkImportUserDataService;
import com.docusign.controller.admin.services.GetExistingAccountIdService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Session session;
    private final User user;

    @Autowired
    public A004BulkImportUserData(DSConfiguration config, Session session, User user) {
        super(config, "a004", "Add users via bulk import");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();
        // Collect ids and user data needed for the request
        UUID organizationId = this.getOrganizationId(accessToken, basePath);
        UUID accountId = GetExistingAccountIdService.getExistingAccountId(
                createUsersApi(accessToken, basePath),
                config.getSignerEmail(),
                organizationId);
        BulkImportsApi bulkImportsApi = createBulkImportsApi(accessToken, basePath);

        OrganizationImportResponse importResponse = BulkImportUserDataService
                .bulkImportUserData(bulkImportsApi, organizationId, accountId);

        this.session.setImportId(importResponse.getId().toString());
        
        // Process results
        DoneExample.createDefault(title)
                .withMessage("Results from UserImport:addBulkUserImport method:")
                .withJsonObject(importResponse)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
