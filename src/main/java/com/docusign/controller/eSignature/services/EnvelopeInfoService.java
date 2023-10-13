package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Envelope;

//ds-snippet-start:eSign4Step2
public final class EnvelopeInfoService {
    public static Envelope envelopeInfo(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        return envelopesApi.getEnvelope(accountId, envelopeId);
    }
}
//ds-snippet-end:eSign4Step2
