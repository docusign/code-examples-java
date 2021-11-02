package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.model.OrganizationImportResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.services.admin.BulkImportUserDataService;
import com.services.admin.GetExistingAccountIdService;
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
        // Collect ids and user data needed for the request
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = GetExistingAccountIdService.getExistingAccountId(
                createUsersApi(this.user.getAccessToken(), this.session.getBasePath()),
                config.getSignerEmail(),
                organizationId);
        BulkImportsApi bulkImportsApi = createBulkImportsApi(this.user.getAccessToken(), this.session.getBasePath());

        OrganizationImportResponse result = BulkImportUserDataService
                .bulkImportUserData(bulkImportsApi, organizationId, accountId);

        this.session.setImportId(result.getId().toString());
        
        // Process results
        DoneExample.createDefault(title)
                .withMessage("Results from UserImport:addBulkUserImport method:")
                .withJsonObject(result)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
