package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.FormLibrariesApi;
import com.docusign.rooms.api.FormLibrariesApi.GetFormLibrariesOptions;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.client.ApiResponse;
import com.docusign.rooms.model.FormLibrarySummary;
import com.docusign.rooms.model.FormLibrarySummaryList;
import com.docusign.rooms.model.FormSummary;
import com.docusign.rooms.model.FormSummaryList;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class GetFormSummaryListService {
    public static List<FormSummary> getFormSummaryList(
            FormLibrariesApi formLibrariesApi,
            String accountId
    ) throws ApiException {
        ApiResponse<FormLibrarySummaryList> formLibrarySummaryList = formLibrariesApi
                .getFormLibrariesWithHttpInfo(accountId, (GetFormLibrariesOptions) null);

        Map<String, List<String>> headers = formLibrarySummaryList.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        List<FormSummary> forms = new ArrayList<>();
        int counter = 0; // Counter variable

        for (FormLibrarySummary formLibrarySummary : formLibrarySummaryList.getData().getFormsLibrarySummaries()) {
            FormSummaryList formSummaryList = formLibrariesApi.getFormLibraryForms(
                    accountId,
                    formLibrarySummary.getFormsLibraryId());
            forms.addAll(formSummaryList.getForms());
            counter += formSummaryList.getForms().size();

            if (counter >= 3) {
                break;
            }
        }

        return forms;
    }
}
