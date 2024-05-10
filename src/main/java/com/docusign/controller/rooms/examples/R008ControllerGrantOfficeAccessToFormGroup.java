package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.GrantOfficeAccessToFormGroupService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.api.OfficesApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormGroupSummaryList;
import com.docusign.rooms.model.OfficeSummaryList;
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

    private FormGroupsApi formGroupsApi;

    public R008ControllerGrantOfficeAccessToFormGroup(DSConfiguration config, Session session, User user) {
        super(config, "r008");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        //ds-snippet-start:Rooms8Step2
        this.formGroupsApi = createFormGroupsApi(
                this.session.getBasePath(), this.user.getAccessToken()
        );
        //ds-snippet-end:Rooms8Step2

        //ds-snippet-start:Rooms8Step3
        OfficesApi officesApi = createOfficesApiClient(this.session.getBasePath(), this.user.getAccessToken());
        OfficeSummaryList officeSummaryList = officesApi.getOffices(this.session.getAccountId());
        //ds-snippet-end:Rooms8Step3

        //ds-snippet-start:Rooms8Step4
        FormGroupSummaryList formGroupSummaryList = formGroupsApi.getFormGroups(this.session.getAccountId());
        //ds-snippet-end:Rooms8Step4

        model.addAttribute(MODEL_FORM_GROUP_LIST, formGroupSummaryList.getFormGroups());
        model.addAttribute(MODEL_OFFICE_LIST, officeSummaryList.getOfficeSummaries());
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {

        try {
            //ds-snippet-start:Rooms8Step5
            GrantOfficeAccessToFormGroupService.grantOfficeAccessToFormGroup(
                    this.formGroupsApi,
                    this.session.getAccountId(),
                    args.getFormGroupId(),
                    args.getOfficeId());
            //ds-snippet-end:Rooms8Step5

            DoneExample.createDefault(this.title)
                    .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                            .replaceFirst("\\{0}", String.valueOf(args.getOfficeId()))
                            .replaceFirst("\\{1}", String.valueOf(args.getFormGroupId())))
                    .addToModel(model, config);
        } catch (ApiException apiException) {
            if (!apiException.getMessage().contains(OFFICE_ALREADY_HAS_ACCESS_TO_FORM_GROUP_ERROR_MESSAGE)) {
                throw apiException;
            }
            DoneExample.createDefault(this.title)
                    .withMessage("The selected office already has access to a form group!")
                    .addToModel(model, config);
        }
        return DONE_EXAMPLE_PAGE;
    }
}
