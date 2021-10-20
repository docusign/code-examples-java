package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkImportsApi;
import com.docusign.admin.model.OrganizationImportResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.services.admin.examples.CheckImportRequestStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.view.RedirectView;


import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * Check request status
 * This example demonstrates how to check request status from the Admin API.
 */
@Controller
@RequestMapping("/a004a")
public class A004CheckImportRequestStatus extends AbstractAdminController {
    private static final String MODEL_IMPORT_OK = "importOk";
    private final Session session;
    private final User user;

    @Autowired
    public A004CheckImportRequestStatus(DSConfiguration config, Session session, User user) {
        super(config, "a004a", "Check request status");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_IMPORT_OK, StringUtils.isNotBlank(this.session.getImportId()));
    }


    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        // Redirects to the front facing example to avoid a Null Pointer Exception
        if(this.session.getImportId() == null){
            return new RedirectView("a004");
        }

        // Create a bulk exports api instance
        BulkImportsApi bulkImportsApi = createBulkImportsApi(this.user.getAccessToken(), this.session.getBasePath());

        OrganizationImportResponse result = CheckImportRequestStatusService.checkRequestStatus(
                bulkImportsApi,
                this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath()),
                UUID.fromString(this.session.getImportId())
        );

        if (result.getStatus().equals("queued")){
            // Return the refresh page
            DoneExample.createDefault("Request not complete")
            .withMessage("The request has not completed, please refresh this page")
            .addToModel(model);
            return EXAMPLE_PENDING_PAGE;

        }

        // Clear the import ID to remove 'Check Status link' from the results page
        this.session.setImportId(null);
        
        // Process results
        DoneExample.createDefault(title)
                .withMessage("Admin API data response output:")
                .withJsonObject(result)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
