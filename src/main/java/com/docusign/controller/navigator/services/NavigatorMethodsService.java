package com.docusign.controller.navigator.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.operations.GetAgreementResponse;
import com.docusign.iam.sdk.models.operations.GetAgreementsListRequest;
import com.docusign.iam.sdk.models.operations.GetAgreementsListResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;

public class NavigatorMethodsService {
	//ds-snippet-start:NavigatorJavaStep2
	private static IamClient createIamClient(String accessToken) {
		return IamClient.builder()
				.accessToken(accessToken)
				.build();
	}
	//ds-snippet-end:NavigatorJavaStep2

	public static GetAgreementsListResponse getAgreements(String accountId, String accessToken) throws Exception {
		return createIamClient(accessToken)
				.navigator()
				.agreements()
				.getAgreementsList()
				.request(new GetAgreementsListRequest(accountId))
				.call();
	}

	public static GetAgreementResponse getAgreement(String accountId, String accessToken, String agreementId)
			throws Exception {
		return createIamClient(accessToken)
				.navigator()
				.agreements()
				.getAgreement()
				.accountId(accountId)
				.agreementId(agreementId)
				.call();
	}

	public static String serializeObjectToJson(Object data) throws Exception {
		var mapper = new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.registerModule(new JsonNullableModule());

		return mapper.writeValueAsString(data);
	}
}
