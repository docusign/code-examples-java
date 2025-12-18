package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDocumentsResult;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class EnvelopeDocsService {
    public static EnvelopeDocumentsResult envelopeDocs(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        // Step 1. List the envelope's documents
        var listDocuments = envelopesApi.listDocumentsWithHttpInfo(accountId, envelopeId, envelopesApi.new ListDocumentsOptions());
        Map<String, List<String>> headers = listDocuments.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return listDocuments.getData();
    }
}
