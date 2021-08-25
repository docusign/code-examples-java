package com.docusign.services.eSignature.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class EnvelopeDocsService {
    public static EnvelopeDocumentsResult envelopeDocs(EnvelopesApi envelopesApi, String accountId, String envelopeId) throws ApiException {
        // Step 1. List the envelope's documents
        return envelopesApi.listDocuments(accountId, envelopeId);
    }
}
