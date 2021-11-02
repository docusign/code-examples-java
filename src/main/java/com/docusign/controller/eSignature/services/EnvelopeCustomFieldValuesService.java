package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.CustomFieldsEnvelope;

public final class EnvelopeCustomFieldValuesService {
    public static CustomFieldsEnvelope envelopeCustomFieldValues(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        return envelopesApi.listCustomFields(accountId, envelopeId);
    }
}
