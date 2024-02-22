package com.docusign.controller.eSignature.services;

import com.docusign.core.utils.DateUtils;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopesInformation;

import java.time.LocalDate;

public final class ListEnvelopesServices {
    private static final int FROM_DATE_OFFSET_DAYS = 30;

    public static EnvelopesInformation listEnvelopes(EnvelopesApi envelopesApi, String accountId) throws ApiException {
        EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(FROM_DATE_OFFSET_DAYS);
        options.setFromDate(DateUtils.DATE_WITH_LINES.format(date));
        return envelopesApi.listStatusChanges(accountId, options);
    }
}
