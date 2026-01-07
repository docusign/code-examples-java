package com.docusign.controller.monitor.services;

import com.docusign.monitor.api.DataSetApi;
import com.docusign.monitor.client.ApiResponse;
import com.docusign.monitor.model.CursoredResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public final class GetMonitoringDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetMonitoringDataService.class);

    public static JSONArray getMonitoringData(DataSetApi datasetApi) throws Exception {
        // Declare variables
        boolean complete = false;
        LocalDate cursorDate = LocalDate.now().minusDays(1);
        String cursorValue = cursorDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "T00:00:00Z";
        JSONArray monitoringData = new JSONArray();

        LOGGER.info("before optinos");
        DataSetApi.GetStreamOptions options = datasetApi.new GetStreamOptions();
        options.setLimit(2000);

        // First call the endpoint with no cursor to get the first records.
        // After each call, save the cursor and use it to make the next
        // call from the point where the previous one left off when iterating through
        // the monitoring records
        try {
            //ds-snippet-start:Monitor1Step3
            do {
                if (!cursorValue.isEmpty()) {
                    options.setCursor(cursorValue);
                }

                LOGGER.info("options set: " + options);
                ApiResponse<CursoredResult> cursoredResult = datasetApi.getStreamWithHttpInfo("2.0", "monitor",
                        options);

                Map<String, List<String>> headers = cursoredResult.getHeaders();
                List<String> remaining = headers.get("X-RateLimit-Remaining");
                List<String> reset = headers.get("X-RateLimit-Reset");

                if (remaining != null & reset != null) {
                    Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
                    System.out.println("API calls remaining: " + remaining);
                    System.out.println("Next Reset: " + resetInstant);
                }

                LOGGER.info("cursor results " + cursoredResult.getData());
                String endCursor = cursoredResult.getData().getEndCursor();

                // If the endCursor from the response is the same as the one that you already
                // have,
                // it means that you have reached the end of the records
                if (endCursor.equals(cursorValue)) {
                    complete = true;
                } else {
                    cursorValue = endCursor;
                    monitoringData.put(new JSONObject(cursoredResult));
                }
            } while (!complete);
            //ds-snippet-end:Monitor1Step3
        } catch (Exception e) {
            LOGGER.error(String.valueOf(e));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Error",
                    "You do not have Monitor enabled for your account, follow <a target='_blank' href='https://developers.docusign.com/docs/monitor-api/how-to/enable-monitor/'>How to enable Monitor for your account</a> to get it enabled.");
            monitoringData.put(jsonObject);
        }

        return monitoringData;
    }
}
