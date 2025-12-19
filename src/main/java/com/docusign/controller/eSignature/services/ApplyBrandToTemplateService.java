package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.TemplateRole;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ApplyBrandToTemplateService {
	public static EnvelopeSummary applyBrandToTemplate(
			EnvelopesApi envelopesApi,
			String accountId,
			EnvelopeDefinition envelope) throws ApiException {
		//ds-snippet-start:eSign30Step4
		var createEnvelopeResponse = envelopesApi.createEnvelopeWithHttpInfo(accountId, envelope,
				envelopesApi.new CreateEnvelopeOptions());
		Map<String, List<String>> headers = createEnvelopeResponse.getHeaders();
		java.util.List<String> remaining = headers.get("X-RateLimit-Remaining");
		List<String> reset = headers.get("X-RateLimit-Reset");

		if (remaining != null & reset != null) {
			Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
			System.out.println("API calls remaining: " + remaining);
			System.out.println("Next Reset: " + resetInstant);
		}

		return createEnvelopeResponse.getData();
		//ds-snippet-end:eSign30Step4
	}

	public static EnvelopeDefinition makeEnvelope(
			String signerEmail,
			String signerName,
			String ccEmail,
			String ccName,
			String templateId,
			String brandId) {
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
