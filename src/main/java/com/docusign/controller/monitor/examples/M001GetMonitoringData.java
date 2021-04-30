package com.docusign.controller.monitor.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * List envelopes in the user's account.<br />
 * List the envelopes created in the last 30 days. This example demonstrates
 * how to query DocuSign about envelopes sent by the current user.
 */
@Controller
@RequestMapping("/m001")
public class M001GetMonitoringData extends AbstractMonitorController {

    private final Session session;
    private final User user;

    @Autowired
    public M001GetMonitoringData(DSConfiguration config, Session session, User user) {
        super(config, "m001", "Get monitoring data");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws IOException {
        String accessToken = this.user.getAccessToken();

        // Check, if you are using the JWT authentication
        // step 1 start
        ensureUsageOfJWTToken(accessToken, this.session);
        // step 1 end

        String requestPath = "https://lens-d.docusign.net/api/v2.0/datasets/monitor/";

        JSONArray result =  getMonitoringData(requestPath, accessToken);

        // Process results
        DoneExample.createDefault(title)
                .withMessage("Monitor data response output:")
                .withJsonObject(result.toString())
                .addToModel(model);

        return DONE_EXAMPLE_PAGE;
    }

    protected JSONArray getMonitoringData(String requestPath, String accessToken) throws IOException {
        // Declare variables
        Boolean complete = false;
        String cursorValue = "";
        Integer limit = 1; // Amount of records you want to read in one request
        JSONArray result = new JSONArray();

        // Get monitoring data
        do
        {
            String cursorValueFormatted = (cursorValue.isEmpty()) ? cursorValue : String.format("=%s", cursorValue);

            // Add cursor value and amount of records to read to the request
            String requestParameters = String.format("stream?cursor%s&limit=%d",
                    cursorValueFormatted, limit);

            URL fullRequestPath = new URL(requestPath + requestParameters);
            HttpURLConnection httpConnection = (HttpURLConnection) fullRequestPath.openConnection();
            httpConnection.setRequestMethod("GET");

            //  Construct API headers
            // step 2 start
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            // step 2 end

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String temp = "";
            StringBuffer stringBuffer = new StringBuffer();
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
            }
            bufferedReader.close();

            httpConnection.disconnect();
            // Cleaning the data from wrong symbols
            String responseData = stringBuffer.toString().replaceAll("'", "");

            JSONObject object = new JSONObject(responseData);
            String endCursor = object.getString("endCursor");

            // If the endCursor from the response is the same as the one that you already have,
            // it means that you have reached the end of the records
            if (endCursor.equals(cursorValue))
            {
                complete = true;
            }
            else
            {
                cursorValue = endCursor;
                result.put(new JSONObject(responseData));
            }
        }
        while (!complete);

        return result;
    }
}
