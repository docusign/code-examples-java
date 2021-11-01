package com.docusign.services.eSignature.examples;

import com.docusign.common.WorkArguments;
import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.TemplateRole;

import java.util.Arrays;

public final class UseTemplateService {
    public static EnvelopeSummary createEnvelopeTemplate(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        return envelopesApi.createEnvelope(accountId, envelope);
    }
    public static EnvelopeDefinition makeEnvelope(
            String signerName,
            String signerEmail,
            String ccEmail,
            String ccName,
            String templateId
    ) {
        TemplateRole signer = new TemplateRole();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        TemplateRole cc = new TemplateRole();
        cc.setEmail(ccEmail);
        cc.setName(ccName);
        cc.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setTemplateId(templateId);
        envelope.setTemplateRoles(Arrays.asList(signer, cc));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelope;
    }
}
