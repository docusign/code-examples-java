package com.docusign.services.eSignature.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopesInformation;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;

public final class ListEnvelopesServices {
    private static final int FROM_DATE_OFFSET_DAYS = 30;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static EnvelopesInformation listEnvelopes(EnvelopesApi envelopesApi, String accountId) throws ApiException {
        EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(FROM_DATE_OFFSET_DAYS);
        options.setFromDate(DATE_FORMATTER.format(date));
        return envelopesApi.listStatusChanges(accountId, options);
    }
}
