package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.api.OfficesApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Granting office access to a form group.
 */
@Controller
@RequestMapping("/r008")
public class R008ControllerGrantOfficeAccessToFormGroup extends AbstractRoomsController {

    private static final String MODEL_OFFICE_LIST = "officeList";
    private static final String MODEL_FORM_GROUP_LIST = "formGroupList";
    private static final String OFFICE_ALREADY_HAS_ACCESS_TO_FORM_GROUP_ERROR_MESSAGE = "OFFICE_ALREADY_HAS_ACCESS_TO_FORM_GROUP";

    private final Session session;
    private final User user;

    private FormGroupsApi formGroupsApi;

    @Autowired
    public R008ControllerGrantOfficeAccessToFormGroup(DSConfiguration config, Session session, User user) {
        super(config, "r008", "Granting office access to a form group");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        this.formGroupsApi = createFormGroupsApi(
                this.session.getBasePath(), this.user.getAccessToken()
        );

        // Step 3 Start
        OfficesApi officesApi = createOfficesApiClient(this.session.getBasePath(), this.user.getAccessToken());
        OfficeSummaryList officeSummaryList = officesApi.getOffices(this.session.getAccountId());
        // Step 3 End

        // Step 4 Start
        FormGroupSummaryList formGroupSummaryList = formGroupsApi.getFormGroups(this.session.getAccountId());
        // Step 4 End

        model.addAttribute(MODEL_FORM_GROUP_LIST, formGroupSummaryList.getFormGroups());
        model.addAttribute(MODEL_OFFICE_LIST, officeSummaryList.getOfficeSummaries());
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {

        try {
            // Step 5 Start
            this.formGroupsApi.grantOfficeAccessToFormGroup(this.session.getAccountId(), args.getFormGroupId(), args.getOfficeId());
            // Step 5 End
            
            DoneExample.createDefault(this.title)
                    .withMessage("Office has been granted access to a form group!")
                    .addToModel(model);
        } catch (ApiException apiException) {
            if (!apiException.getMessage().contains(OFFICE_ALREADY_HAS_ACCESS_TO_FORM_GROUP_ERROR_MESSAGE)) {
                throw apiException;
            }
            DoneExample.createDefault(this.title)
                    .withMessage("The selected office already has access to a form group!")
                    .addToModel(model);
        }
        return DONE_EXAMPLE_PAGE;
    }
}
