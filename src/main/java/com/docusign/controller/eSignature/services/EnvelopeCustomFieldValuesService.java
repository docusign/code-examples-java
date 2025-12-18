package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.CustomFieldsEnvelope;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class EnvelopeCustomFieldValuesService {
    public static CustomFieldsEnvelope envelopeCustomFieldValues(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        var customFields = envelopesApi.listCustomFieldsWithHttpInfo(accountId, envelopeId);
        Map<String, List<String>> headers = customFields.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return customFields.getData();
    }
}
