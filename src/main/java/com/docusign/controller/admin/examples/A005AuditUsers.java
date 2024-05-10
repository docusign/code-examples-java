package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.UserDrilldownResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.AuditUsersService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Create new user This example demonstrates how to create a new user with
 * methods from Admin API.
 */
@Controller
@RequestMapping("/a005")
public class A005AuditUsers extends AbstractAdminController {

    public A005AuditUsers(DSConfiguration config, Session session, User user) {
        super(config, "a005", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        // Collect ids needed for the request
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        UUID accountId = this.getExistingAccountId(this.user.getAccessToken(), this.session.getBasePath(), organizationId);

        UsersApi usersApi = createUsersApi(this.user.getAccessToken(), this.session.getBasePath());

        ArrayList<UserDrilldownResponse> resultsArr = AuditUsersService.auditUsers(usersApi, organizationId, accountId);

        // Process results
        DoneExample
                .createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(resultsArr).addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }

}
