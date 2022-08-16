package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.RetrieveDocuSignProfileByEmailAddress;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final User user;

    @Autowired
    public A006RetrieveDocuSignProfileByEmailAddress(DSConfiguration config, User user) {
        super(config, "a006");
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        // Step 3 start
        UsersApi usersApi = createUsersApi(this.user.getAccessToken(), this.session.getBasePath());
        UsersDrilldownResponse usersResponse = RetrieveDocuSignProfileByEmailAddress
                .getDocuSignProfileByEmailAddress(usersApi, organizationId, args.getEmail());
        // Step 3 end
        DoneExample
                .createDefault(this.codeExampleText.ResultsPageHeader)
                .withMessage(this.codeExampleText.ResultsPageText)
                .withJsonObject(usersResponse.getUsers()).addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }
}
