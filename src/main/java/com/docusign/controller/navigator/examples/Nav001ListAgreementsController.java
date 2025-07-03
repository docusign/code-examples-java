package com.docusign.controller.navigator.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.navigator.services.NavigatorMethodsService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * This example demonstrates how to get all agreements.
 */
@Controller
@RequestMapping("/nav001")
public class Nav001ListAgreementsController extends AbstractNavigatorController {

    public Nav001ListAgreementsController(DSConfiguration config, Session session, User user) {
        super(config, "nav001", user, session);
    }

    @Override
    //ds-snippet-start:Navigator1Step3
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        var accountId = session.getAccountId();
        var accessToken = user.getAccessToken();

        var agreements = NavigatorMethodsService.getAgreements(accountId, accessToken);
        var agreementsData = agreements.agreementsResponse().orElseThrow().data().orElseThrow();
        var jsonAgreements = NavigatorMethodsService.serializeObjectToJson(agreementsData);
    //ds-snippet-end:Navigator1Step3

        DoneExample.createDefault(getTextForCodeExampleByApiType().ExampleName)
                .withMessage(getTextForCodeExampleByApiType().ResultsPageText)
                .withJsonObject(jsonAgreements)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
