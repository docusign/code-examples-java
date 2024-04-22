package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.RetrieveDocuSignProfileByEmailAddress;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Get a DocuSign user by the email address using the methods from Admin API.
 */
@Controller
@RequestMapping("/a006")
public class A006RetrieveDocuSignProfileByEmailAddress extends AbstractAdminController {

    public A006RetrieveDocuSignProfileByEmailAddress(DSConfiguration config, User user, Session session) {
        super(config, "a006", user, session);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        //ds-snippet-start:Admin6Step3
        UsersApi usersApi = createUsersApi(this.user.getAccessToken(), this.session.getBasePath());
        UsersDrilldownResponse usersResponse = RetrieveDocuSignProfileByEmailAddress
                .getDocuSignProfileByEmailAddress(usersApi, organizationId, args.getEmail());
        //ds-snippet-end:Admin6Step3
        DoneExample
                .createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(usersResponse.getUsers()).addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
