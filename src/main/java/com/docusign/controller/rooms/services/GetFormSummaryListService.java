package com.docusign.controller.rooms.services;

import com.docusign.rooms.api.FormLibrariesApi;
import com.docusign.rooms.client.ApiException;
import com.docusign.rooms.model.FormLibrarySummary;
import com.docusign.rooms.model.FormLibrarySummaryList;
import com.docusign.rooms.model.FormSummary;
import com.docusign.rooms.model.FormSummaryList;

import java.util.ArrayList;
import java.util.List;


public final class GetFormSummaryListService {
    public static List<FormSummary> getFormSummaryList(
            FormLibrariesApi formLibrariesApi,
            String accountId
    ) throws ApiException {
        FormLibrarySummaryList formLibrarySummaryList = formLibrariesApi.getFormLibraries(accountId);

        List<FormSummary> forms = new ArrayList<>();
        int counter = 0; // Counter variable

        for (FormLibrarySummary formLibrarySummary : formLibrarySummaryList.getFormsLibrarySummaries()) {
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
