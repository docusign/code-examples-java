package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.BulkExportsApi;
import com.docusign.admin.model.OrganizationExportResponse;
import com.docusign.admin.model.OrganizationExportsResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.BulkExportUserDataService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * How to bulk-export user data This example demonstrates how to bulk-export
 * user data from Admin API.
 */
@Controller
@RequestMapping("/a003")
public class A003BulkExportUserData extends AbstractAdminController {

    private static final int BUFFER_SIZE = 4096;

    public A003BulkExportUserData(DSConfiguration config, Session session, User user) {
        super(config, "a003", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        //ds-snippet-start:Admin3Step3
        BulkExportsApi bulkExportsApi = createBulkExportsApi(this.user.getAccessToken(), this.session.getBasePath());

        OrganizationExportResponse bulkList = BulkExportUserDataService.createUserListExport(
                bulkExportsApi,
                this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath()));
        //ds-snippet-end:Admin3Step3

        TimeUnit.SECONDS.sleep(30);

        //ds-snippet-start:Admin3Step4
        OrganizationExportResponse data = BulkExportUserDataService.bulkExportUserData(
                bulkExportsApi,
                this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath()),
                bulkList.getId());
        //ds-snippet-end:Admin3Step4

        String saveFilePath = "";

        if (data.getResults() != null && !data.getResults().isEmpty()) {
            String csvUri = data.getResults().get(0).getUrl();
            saveFilePath = BulkExportUserDataService.moveUserListExportToFile(
                    csvUri,
                    BEARER_AUTHENTICATION,
                    this.user.getAccessToken(),
                    BUFFER_SIZE
            );
        }

        OrganizationExportsResponse results = BulkExportUserDataService
                .bulkExportsUserData(bulkExportsApi, this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath()));
        // Process results
        DoneExample
                .createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText.replaceFirst("\\{0}", saveFilePath))
                .withJsonObject(results).addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }

}
