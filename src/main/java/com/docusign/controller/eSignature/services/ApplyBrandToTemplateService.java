package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.TemplateRole;

import java.util.Arrays;

public final class ApplyBrandToTemplateService {
    public static EnvelopeSummary applyBrandToTemplate(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        //ds-snippet-start:eSign30Step4
        return envelopesApi.createEnvelope(accountId, envelope);
        //ds-snippet-end:eSign30Step4
    }

    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String ccEmail,
            String ccName,
            String templateId,
            String brandId
    ) {
        TemplateRole signer = new TemplateRole()
                .email(signerEmail)
                .name(signerName)
                .roleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        TemplateRole cc = new TemplateRole()
                .email(ccEmail)
                .name(ccName)
                .roleName(EnvelopeHelpers.CC_ROLE_NAME);

        return new EnvelopeDefinition()
                .templateId(templateId)
                .templateRoles(Arrays.asList(signer, cc))
                .brandId(brandId)
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
    }
}
