package com.docusign.controller.admin.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.api.UsersApi;
import com.docusign.admin.model.UsersDrilldownResponse;
import com.docusign.common.WorkArguments;
import com.docusign.controller.admin.services.RetrieveDocuSignProfileByUserId;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Get a DocuSign user by the user ID using the methods from Admin API.
 */
@Controller
@RequestMapping("/a007")
public class A007RetrieveDocuSignProfileByUserID extends AbstractAdminController {

    private final User user;

    @Autowired
    public A007RetrieveDocuSignProfileByUserID(DSConfiguration config, User user) {
        super(config, "a007");
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        UUID organizationId = this.getOrganizationId(this.user.getAccessToken(), this.session.getBasePath());
        // Step 3 start
        UsersApi usersApi = createUsersApi(this.user.getAccessToken(), this.session.getBasePath());

        UsersDrilldownResponse usersResponse = RetrieveDocuSignProfileByUserId.
                getDocuSignProfileByUserId(usersApi, organizationId, args.getUserId());
        // Step 3 end
        DoneExample
                .createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(usersResponse.getUsers()).addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }

}
