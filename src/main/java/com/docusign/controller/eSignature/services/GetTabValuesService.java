package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeFormData;

public final class GetTabValuesService {
    public static EnvelopeFormData getTabValues(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        return envelopesApi.getFormData(accountId, envelopeId);
    }
}
