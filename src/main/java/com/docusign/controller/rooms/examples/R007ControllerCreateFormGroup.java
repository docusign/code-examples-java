package com.docusign.controller.rooms.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.rooms.services.CreateFormGroupService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.rooms.api.FormGroupsApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormGroup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Creating create a form group.
 */
@Controller
@RequestMapping("/r007")
public class R007ControllerCreateFormGroup extends AbstractRoomsController {

    public R007ControllerCreateFormGroup(DSConfiguration config, Session session, User user) {
        super(config, "r007");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws IOException, ApiException {

        //ds-snippet-start:Rooms7Step2
        FormGroupsApi formGroupsApi = createFormGroupsApi(
                this.session.getBasePath(), this.user.getAccessToken()
        );
        //ds-snippet-end:Rooms7Step2

        FormGroup formGroup = CreateFormGroupService.createFormGroup(
                formGroupsApi,
                this.session.getAccountId(),
                args.getFormGroupName());

        DoneExample.createDefault(this.title)
                .withJsonObject(formGroup)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", formGroup.getName()))
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
