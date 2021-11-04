package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.model.OrganizationExportResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.controller.admin.services.CheckRequestStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Check request status
 * This example demonstrates how to check request status from the Admin API.
 */
@Controller
@RequestMapping("/a003a")
public class A003aCheckRequestStatus extends AbstractAdminController {
    private static final String MODEL_EXPORT_OK = "exportOk";

    private final Session session;
    private final User user;

    @Autowired
    public A003aCheckRequestStatus(DSConfiguration config, Session session, User user) {
        super(config, "a003a", "Check request status");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_EXPORT_OK, StringUtils.isNotBlank(this.session.getExportId()));
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();
        // Create a bulk exports api instance
        BulkExportsApi bulkExportsApi = createBulkExportsApi(accessToken, basePath);
        UUID organizationId = this.getOrganizationId(accessToken, basePath);
        UUID exportId = UUID.fromString(this.session.getExportId());

        OrganizationExportResponse exportResponse = CheckRequestStatusService.checkRequestStatus(
                bulkExportsApi,
                organizationId,
                exportId);

        // Process results
        DoneExample.createDefault(title)
                .withMessage("Admin API data response output:")
                .withJsonObject(exportResponse)
                .addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
