package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;

import java.time.Instant;
import java.util.List;
import java.util.Map;

//ds-snippet-start:eSign4Step2
public final class EnvelopeInfoService {
    public static Envelope envelopeInfo(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId) throws ApiException {
        var envelopeResponse = envelopesApi.getEnvelopeWithHttpInfo(accountId, envelopeId,
                envelopesApi.new GetEnvelopeOptions());
        Map<String, List<String>> headers = envelopeResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return envelopeResponse.getData();
    }
}
//ds-snippet-end:eSign4Step2
