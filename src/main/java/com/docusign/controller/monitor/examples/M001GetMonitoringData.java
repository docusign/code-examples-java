package com.docusign.controller.monitor.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.monitor.api.DataSetApi;
import com.docusign.monitor.api.DataSetApi.GetStreamOptions;
import com.docusign.monitor.model.CursoredResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


/**
 * Get monitoring data<br />
 * This example demonstrates how to get monitoring data from the Monitor API.
 */
@Controller
@RequestMapping("/m001")
public class M001GetMonitoringData extends AbstractMonitorController {

    private final Session session;
    private final User user;

    @Autowired
    public M001GetMonitoringData(DSConfiguration config, Session session, User user) {
        super(config, "m001", "Monitoring data result");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws Exception {
        String accessToken = this.user.getAccessToken();

        // Check, if you are using the JWT authentication
        // step 1 start
        accessToken = ensureUsageOfJWTToken(accessToken, this.session);
        // step 1 end

        JSONArray result =  getMonitoringData(accessToken);

        // Cleaning the data from wrong symbols
        String resultCleaned = result.toString().replaceAll("'", "");

        // Process results
        DoneExample.createDefault(title)
                .withMessage("Results from DataSet:getStream method:")
                .withJsonObject(resultCleaned)
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    protected JSONArray getMonitoringData(String accessToken) throws Exception {
        // Declare variables
        boolean complete = false;
        String cursorValue = "";
//        Integer limit = 1; // Amount of records you want to read in one request
        JSONArray result = new JSONArray();

        DataSetApi datasetApi = this.createDataSetApi(accessToken, this.session);
        GetStreamOptions options = datasetApi.new GetStreamOptions();
//        options.setLimit(limit);

        // First call the endpoint with no cursor to get the first records.
        // After each call, save the cursor and use it to make the next
        // call from the point where the previous one left off when iterating through
        // the monitoring records
        do
        {
            if(!cursorValue.isEmpty())
                options.setCursor(cursorValue);

            CursoredResult cursoredResult = datasetApi.getStream("2.0", "monitor", options);

            String endCursor = cursoredResult.getEndCursor();

            // If the endCursor from the response is the same as the one that you already have,
            // it means that you have reached the end of the records
            if (endCursor.equals(cursorValue))
            {
                complete = true;
            }
            else
            {
                cursorValue = endCursor;
                result.put(new JSONObject(cursoredResult));
            }
        }
        while (!complete);

        return result;
    }
}
