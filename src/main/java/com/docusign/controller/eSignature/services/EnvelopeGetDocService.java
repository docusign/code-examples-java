package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import org.apache.commons.io.FilenameUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class EnvelopeGetDocService {

    public static byte[] envelopeGetDoc(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            String documentId) throws ApiException {
        // ds-snippet-start:eSign7Step3
        var documentResponse = envelopesApi.getDocumentWithHttpInfo(accountId, envelopeId, documentId,
                envelopesApi.new GetDocumentOptions());
        Map<String, List<String>> headers = documentResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return documentResponse.getData();
        // ds-snippet-end:eSign7Step3
    }

    public static String addExtension(String fileName, String extension) {
        if (FilenameUtils.isExtension(fileName, extension)) {
            return fileName;
        }
        return String.join(".", fileName, extension);
    }
}
