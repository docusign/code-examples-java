package com.services.eSignature;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;

public final class EnvelopeInfoService {
    public static Envelope envelopeInfo(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        return envelopesApi.getEnvelope(accountId, envelopeId);
    }
}
