package com.docusign.controller.navigator.examples;

import com.docusign.DSConfiguration;
import com.docusign.admin.client.ApiException;
import com.docusign.common.WorkArguments;
import com.docusign.controller.navigator.services.NavigatorMethodsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to get agreement by id.
 */
@Controller
@RequestMapping("/nav002")
public class Nav002GetSingleAgreementController extends AbstractNavigatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Nav002GetSingleAgreementController.class);

    private static final String MODEL_AGREEMENTS_LIST = "agreements";

    public Nav002GetSingleAgreementController(DSConfiguration config, Session session, User user) {
        super(config, "nav002", user, session);
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        try {
            super.onInitModel(args, model);

            var accountId = session.getAccountId();
            var accessToken = user.getAccessToken();

            var agreements = NavigatorMethodsService.getAgreements(accountId, accessToken);
            var agreementsData = agreements.agreementsResponse().orElseThrow().data().orElseThrow();

            model.addAttribute(MODEL_AGREEMENTS_LIST, agreementsData);
        } catch (ApiException e) {
            LOGGER.info(String.valueOf(e));
        }
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();
        var agreementId = args.getAgreementId();

        var agreement = NavigatorMethodsService.getAgreement(accountId, accessToken, agreementId);
        var jsonAgreement = NavigatorMethodsService.serializeObjectToJson(agreement.agreement().orElseThrow());

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(jsonAgreement)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
