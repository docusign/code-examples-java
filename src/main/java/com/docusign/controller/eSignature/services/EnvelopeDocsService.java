package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDocumentsResult;

public final class EnvelopeDocsService {
    //ds-snippet-start:eSign6Step3
    public static EnvelopeDocumentsResult envelopeDocs(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        // List the envelope's documents
        return envelopesApi.listDocuments(accountId, envelopeId);
    }
    //ds-snippet-end:eSign6Step3
}
