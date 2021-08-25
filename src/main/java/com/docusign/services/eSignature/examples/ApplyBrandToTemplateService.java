package com.docusign.services.eSignature.examples;

import com.docusign.common.WorkArguments;
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
        return envelopesApi.createEnvelope(accountId, envelope);
    }

    public static EnvelopeDefinition makeEnvelope(WorkArguments args) {
        TemplateRole signer = new TemplateRole()
                .email(args.getSignerEmail())
                .name(args.getSignerName())
                .roleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        TemplateRole cc = new TemplateRole()
                .email(args.getCcEmail())
                .name(args.getCcName())
                .roleName(EnvelopeHelpers.CC_ROLE_NAME);

        return new EnvelopeDefinition()
                .templateId(args.getTemplateId())
                .templateRoles(Arrays.asList(signer, cc))
                .brandId(args.getBrandId())
                .status(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
    }
}
