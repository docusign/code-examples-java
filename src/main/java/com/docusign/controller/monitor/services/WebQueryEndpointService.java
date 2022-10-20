package com.docusign.controller.monitor.services;

import com.docusign.monitor.api.DataSetApi;
import com.docusign.monitor.model.AggregateResult;
import com.docusign.monitor.model.AggregateResultResult;
import com.docusign.monitor.model.WebQuery;
import com.docusign.monitor.client.ApiException;
import org.json.JSONObject;

import java.util.List;


public final class WebQueryEndpointService {
    // Step 4 start
    private static final String API_VERSION = "2.0";
    private static final String NAME_OF_DATASET = "monitor";
    public static JSONObject postWebQueryMethod(
            DataSetApi datasetApi,
            String accountId,
            String filterStartDate,
            String filterEndDate) throws ApiException {
        try {
            AggregateResult aggregateResult = datasetApi.postWebQuery(
                    API_VERSION,
                    NAME_OF_DATASET,
                    prepareWebQueryOptions(accountId, filterStartDate, filterEndDate));
            
            AggregateResultResult methodResult = null;
            List<AggregateResultResult> aggregateResultList = aggregateResult.getResult();
            // Step 4 end
            if (aggregateResultList != null && !aggregateResultList.isEmpty()) {
                methodResult = aggregateResultList.get(0);
            }

            return new JSONObject(methodResult);
        }

        catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Error", "You do not have Monitor enabled for your account, follow <a target='_blank' href='https://developers.docusign.com/docs/monitor-api/how-to/enable-monitor/'>How to enable Monitor for your account</a> to get it enabled.");
            return jsonObject;
        }
    }

    // Step 3 start
    private static WebQuery prepareWebQueryOptions(String accountId, String filterStartDate, String filterEndDate) {
        WebQuery webQuery = new WebQuery();
        webQuery.addFiltersItem(new Object(){
            public String FilterName = "Time";
            public String BeginTime = filterStartDate;
            public String EndTime = filterEndDate;
        });
        webQuery.addFiltersItem(new Object(){
            public String FilterName = "Has";
            public String ColumnName = "AccountId";
            public String Value = accountId;
        });
        webQuery.addAggregationsItem(new Object(){
            public String aggregationName = "Raw";
            public String limit = "100";
            public String[] orderby = new String[] { "Timestamp, desc" };
        });
        return webQuery;
    }
    // Step 3 end
}

