package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.model.OrganizationExportResponse;
import com.docusign.admin.model.OrganizationExportsResponse;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.controller.admin.services.BulkExportUserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * How to bulk-export user data This example demonstrates how to bulk-export
 * user data from Admin API.
 */
@Controller
@RequestMapping("/a003")
public class A003BulkExportUserData extends AbstractAdminController {

    private static final int BUFFER_SIZE = 4096;
    private final Session session;
    private final User user;

    @Autowired
    public A003BulkExportUserData(DSConfiguration config, Session session, User user) {
        super(config, "a003", "Bulk export user data");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();
        String basePath = this.session.getBasePath();
        // Step 3 start
        BulkExportsApi bulkExportsApi = createBulkExportsApi(accessToken, basePath);
        UUID organizationId = this.getOrganizationId(accessToken, basePath);

        OrganizationExportResponse bulkList = BulkExportUserDataService.createUserListExport(
                bulkExportsApi,
                organizationId);
        // Step 3 end

        TimeUnit.SECONDS.sleep(20);

        // Step 4 start
        String csvUri = BulkExportUserDataService.bulkExportUserData(bulkExportsApi, organizationId, bulkList.getId());
        // Step 4 end

        String saveFilePath = BulkExportUserDataService.moveUserListExportToFile(
                csvUri,
                BEARER_AUTHENTICATION,
                accessToken,
                BUFFER_SIZE);

        OrganizationExportsResponse exportResponse = BulkExportUserDataService.bulkExportsUserData(
                bulkExportsApi,
                organizationId);
        // Process results
        DoneExample.createDefault(title)
                .withMessage("User data exported to " + saveFilePath + "<br>from UserExport:getUserListExport method:")
                .withJsonObject(exportResponse).addToModel(model);
        return DONE_EXAMPLE_PAGE;
    }
}
