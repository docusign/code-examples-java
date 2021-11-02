package com.services.monitor;

import com.docusign.monitor.api.DataSetApi;
import com.docusign.monitor.model.CursoredResult;
import org.json.JSONArray;
import org.json.JSONObject;

public final class GetMonitoringDataService {
    public static JSONArray getMonitoringData(DataSetApi datasetApi) throws Exception {
        // Declare variables
        boolean complete = false;
        String cursorValue = "";
        JSONArray result = new JSONArray();

        DataSetApi.GetStreamOptions options = datasetApi.new GetStreamOptions();

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
