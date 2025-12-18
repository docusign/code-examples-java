package com.docusign.controller.eSignature.services;

import com.docusign.core.utils.DateUtils;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopesInformation;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public final class ListEnvelopesServices {
    private static final int FROM_DATE_OFFSET_DAYS = 30;

    public static EnvelopesInformation listEnvelopes(EnvelopesApi envelopesApi, String accountId) throws ApiException {
        EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(FROM_DATE_OFFSET_DAYS);
        options.setFromDate(DateUtils.DATE_WITH_LINES.format(date));

        var listStatus = envelopesApi.listStatusChangesWithHttpInfo(accountId, options);
        Map<String, List<String>> headers = listStatus.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return listStatus.getData();
    }
}
