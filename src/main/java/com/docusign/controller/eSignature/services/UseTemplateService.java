package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.TemplateRole;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class UseTemplateService {
    //ds-snippet-start:eSign9Step3
    public static EnvelopeSummary createEnvelopeTemplate(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope) throws ApiException {
        var createEnvelopeResponse = envelopesApi.createEnvelopeWithHttpInfo(accountId, envelope,
                envelopesApi.new CreateEnvelopeOptions());
        Map<String, List<String>> headers = createEnvelopeResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return createEnvelopeResponse.getData();
    }
    //ds-snippet-end:eSign9Step3

    public static EnvelopeTemplateResults listTemplates(
            ApiClient apiClient,
            String accountId) throws ApiException {
        TemplatesApi templatesApi = new TemplatesApi(apiClient);

        var listTemplatesResponse = templatesApi.listTemplatesWithHttpInfo(accountId,
                templatesApi.new ListTemplatesOptions());
        Map<String, java.util.List<String>> headers = listTemplatesResponse.getHeaders();
        java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
        java.util.List<String> reset = headers.get("X-RateLimit-Reset");

        if (remaining != null & reset != null) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }
        return listTemplatesResponse.getData();
    }

    //ds-snippet-start:eSign9Step2
    public static EnvelopeDefinition makeEnvelope(
            String signerName,
            String signerEmail,
            String ccEmail,
            String ccName,
            String templateId) {
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
    //ds-snippet-end:eSign9Step2
}
