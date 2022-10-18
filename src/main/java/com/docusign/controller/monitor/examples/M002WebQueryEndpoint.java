package com.docusign.controller.monitor.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.controller.monitor.services.WebQueryEndpointService;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Get monitoring data<br />
 * This example demonstrates how to get monitoring data from the Monitor API.
 */
@Controller
@RequestMapping("/m002")
public class M002WebQueryEndpoint extends AbstractMonitorController {

    private static final String MODEL_START_DATE = "startDate";
    private static final String MODEL_END_DATE = "endDate";
    private static final int FROM_DATE_OFFSET_DAYS = 10;
    private static final int FROM_DATE_FORWARD_DAYS = 1;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Session session;
    private final User user;

    @Autowired
    public M002WebQueryEndpoint(DSConfiguration config, Session session, User user) {
        super(config, "m002");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);

        LocalDate endDate = LocalDate.now().plusDays(FROM_DATE_FORWARD_DAYS);
        LocalDate startDate = LocalDate.now().minusDays(FROM_DATE_OFFSET_DAYS);

        model.addAttribute(MODEL_START_DATE, DATE_FORMATTER.format(startDate));
        model.addAttribute(MODEL_END_DATE, DATE_FORMATTER.format(endDate));
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();

        // Check, if you are using the JWT authentication
        // step 1 start
        accessToken = ensureUsageOfJWTToken(accessToken, this.session);
        String accountId = session.getAccountId();
        // step 1 end

        JSONObject queryResult = WebQueryEndpointService.postWebQueryMethod(
                createDataSetApi(accessToken, this.session),
                accountId,
                args.getStartDate(),
                args.getEndDate()
        );

        if (queryResult.has("Error"))
        {
            new DoneExample()
                    .withTitle(getTextForCodeExample().ExampleName)
                    .withName("")
                    .withMessage(queryResult.getString("Error"))
                    .addToModel(model, config);            
            return ERROR_PAGE;
        }
        // Cleaning the data from unsupported symbols
        String queryResultCleaned = queryResult.toString().replaceAll("'", "");

        // Process results
        DoneExample.createDefault(getTextForCodeExample().ExampleName)
                .withMessage(getTextForCodeExample().ResultsPageText)
                .withJsonObject(queryResultCleaned)
                .addToModel(model, config);

        return DONE_EXAMPLE_PAGE;
    }
}
