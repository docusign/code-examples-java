package com.docusign.controller.connectedFields.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.client.ApiException;
import com.docusign.common.WorkArguments;
import com.docusign.controller.connectedFields.services.SetConnectedFieldsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to set connected fields from extension.
 */
@Controller
@RequestMapping("/cf001")
public class CF001SetConnectedFields extends AbstractConnectedFieldsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CF001SetConnectedFields.class);

    private static final String MODEL_APPS_LIST = "appsList";

    public CF001SetConnectedFields(DSConfiguration config, Session session, User user) {
        super(config, "cf001", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {

        try {
            super.onInitModel(args, model);

            var extensionApps = SetConnectedFieldsService.getConnectedFieldsTabGroups(
                    session.getAccountId(),
                    user.getAccessToken());

            var filteredExtensionApps = SetConnectedFieldsService.filterData(extensionApps);

            this.session.setExtensionApps(filteredExtensionApps);
            model.addAttribute(MODEL_APPS_LIST, filteredExtensionApps);
        } catch (ApiException e) {
            LOGGER.info(String.valueOf(e));
        }
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {

        var extensionApp = SetConnectedFieldsService.findAppById(
                this.session.getExtensionApps(),
                args.getAppId());
        //ds-snippet-start:ConnectedFields1Step6
        EnvelopesApi envelopesApi = createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        EnvelopeDefinition envelope = SetConnectedFieldsService.makeEnvelope(
                args.getSignerEmail(),
                args.getSignerName(),
                extensionApp);
        EnvelopeSummary envelopeSummary = SetConnectedFieldsService.signingViaEmail(
                envelopesApi,
                session.getAccountId(),
                envelope);
        //ds-snippet-end:ConnectedFields1Step6

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText
                        .replaceFirst("\\{0}", envelopeSummary.getEnvelopeId()))
                .addToModel(model, config);
        return DONE_EXAMPLE_PAGE;
    }
}
