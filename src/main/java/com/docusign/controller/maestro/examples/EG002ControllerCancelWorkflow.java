package com.docusign.controller.maestro.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.maestro.services.CancelWorkflowInstanceService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.client.ApiException;
import com.docusign.maestro.client.ApiClient;
import com.docusign.maestro.model.CancelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/mae002")
public class EG002ControllerCancelWorkflow extends AbstractMaestroController {

    private static final String MODEL_WORKFLOW_ID = "workflowId";

    private static final String MODEL_INSTANCE_ID = "instanceId";

    @Autowired
    public EG002ControllerCancelWorkflow(DSConfiguration config, Session session, User user) {
        super(config, "mae002", session, user);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        model.addAttribute(MODEL_WORKFLOW_ID, session.getWorkflowId());
        model.addAttribute(MODEL_INSTANCE_ID, session.getInstanceId());
    }

    @Override
    protected Object doWork(
            WorkArguments args,
            ModelMap model,
            HttpServletResponse response
    ) throws ApiException, IOException, NoSuchAlgorithmException, InvalidKeyException, com.docusign.maestro.client.ApiException {
        //ds-snippet-start:Maestro2Step2
        ApiClient apiClient = createApiClient(config.getMaestroBasePath(), user.getAccessToken());
        //ds-snippet-end:Maestro2Step2

        CancelResponse cancelResponse = CancelWorkflowInstanceService.cancelWorkflowInstance(
                apiClient,
                session.getAccountId(),
                session.getInstanceId());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", session.getInstanceId()))
                .withJsonObject(cancelResponse)
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
