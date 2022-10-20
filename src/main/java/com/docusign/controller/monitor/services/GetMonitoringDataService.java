package com.docusign.controller.monitor.services;

import com.docusign.monitor.api.DataSetApi;
import com.docusign.monitor.model.CursoredResult;
import org.json.JSONArray;
import org.json.JSONObject;

public final class GetMonitoringDataService {
    public static JSONArray getMonitoringData(DataSetApi datasetApi) throws Exception {
        // Declare variables
        boolean complete = false;
        String cursorValue = "";
        JSONArray monitoringData = new JSONArray();

        DataSetApi.GetStreamOptions options = datasetApi.new GetStreamOptions();

        // First call the endpoint with no cursor to get the first records.
        // After each call, save the cursor and use it to make the next
        // call from the point where the previous one left off when iterating through
        // the monitoring records
        try {
            
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
                    monitoringData.put(new JSONObject(cursoredResult));
                }
            }
            while (!complete);
        }
    catch (Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Error", "You do not have Monitor enabled for your account, follow <a target='_blank' href='https://developers.docusign.com/docs/monitor-api/how-to/enable-monitor/'>How to enable Monitor for your account</a> to get it enabled.");
        monitoringData.put(jsonObject);
    }

        return monitoringData;
    }
}
