package com.docusign.controller.eSignature.services;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Recipients;

public final class EnvelopeRecipientsService {
    public static Recipients envelopeRecipients(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId
    ) throws ApiException {
        return envelopesApi.listRecipients(accountId, envelopeId);
    }
}
