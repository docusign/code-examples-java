package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.AssignFormToFormGroupService;
import com.docusign.controller.rooms.services.GetFormSummaryListService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormGroupFormToAssign;
import com.docusign.rooms.model.FormGroupSummaryList;
import com.docusign.rooms.model.FormSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Assigning a form to a form group.
 */
@Controller
@RequestMapping("/r009")
public class R009ControllerAssignFormToFormGroup extends AbstractRoomsController {

    private static final String MODEL_FORM_LIST = "formList";

    private static final String MODEL_FORM_GROUP_LIST = "formGroupList";

    private static final String FORM_ALREADY_EXISTS_ERROR_MESSAGE = "Form in the form group already exists";

    private FormGroupsApi formGroupsApi;

    public R009ControllerAssignFormToFormGroup(DSConfiguration config, Session session, User user) {
        super(config, "r009");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        //ds-snippet-start:Rooms9Step4
        this.formGroupsApi = createFormGroupsApi(
                this.session.getBasePath(), this.user.getAccessToken()
        );
        FormGroupSummaryList formGroupSummaryList = formGroupsApi.getFormGroups(this.session.getAccountId());
        //ds-snippet-end:Rooms9Step4

        //ds-snippet-start:Rooms9Step3
        List<FormSummary> forms = GetFormSummaryListService.getFormSummaryList(
                createFormLibrariesApi(session.getBasePath(), this.user.getAccessToken()),
                this.session.getAccountId());
        //ds-snippet-end:Rooms9Step3

        model.addAttribute(MODEL_FORM_GROUP_LIST, formGroupSummaryList.getFormGroups());
        model.addAttribute(MODEL_FORM_LIST, forms);
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {
        try {
            //ds-snippet-start:Rooms9Step6
            FormGroupFormToAssign formGroupFormToAssign = AssignFormToFormGroupService.assignFormToFormGroup(
                    this.formGroupsApi,
                    this.session.getAccountId(),
                    args.getFormId(),
                    args.getFormGroupId());
            //ds-snippet-end:Rooms9Step6

            DoneExample.createDefault(this.title)
                    .withJsonObject(formGroupFormToAssign)
                    .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                            .replaceFirst("\\{0}", String.valueOf(args.getFormId()))
                            .replaceFirst("\\{1}", String.valueOf(args.getFormGroupId())))
                    .addToModel(model, config);
        } catch (ApiException apiException) {
            if (apiException.getMessage().contains(FORM_ALREADY_EXISTS_ERROR_MESSAGE)) {
                DoneExample.createDefault(this.title)
                        .withMessage(FORM_ALREADY_EXISTS_ERROR_MESSAGE)
                        .addToModel(model, config);
            } else if (apiException.getMessage().contains(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessageCheck)) {
                DoneExample.createDefault(this.title)
                        .withMessage(getTextForCodeExampleByApiType().CustomErrorTexts.get(0).ErrorMessage)
                        .addToModel(model, config);
            }

            throw apiException;
        }
        return DONE_EXAMPLE_PAGE;
    }
}
