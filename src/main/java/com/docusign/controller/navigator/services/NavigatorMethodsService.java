package com.docusign.controller.navigator.services;

import com.docusign.core.model.BulkUploadJobInfo;
import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.CreateBulkJob;
import com.docusign.iam.sdk.models.operations.GetAgreementResponse;
import com.docusign.iam.sdk.models.operations.GetAgreementsListRequest;
import com.docusign.iam.sdk.models.operations.GetAgreementsListResponse;
import com.docusign.iam.sdk.models.operations.UploadCompleteBulkJobResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;

import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.logging.Logger;

public class NavigatorMethodsService {
	private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

	//ds-snippet-start:NavigatorJavaStep2
	private static IamClient createIamClient(String accessToken) {
		return IamClient.builder()
			.accessToken(accessToken)
			.build();
	}
	//ds-snippet-end:NavigatorJavaStep2

	public static GetAgreementsListResponse getAgreements(String accountId, String accessToken) throws Exception {
		return createIamClient(accessToken)
			.agreementManager()
			.agreements()
			.getAgreementsList()
			.request(new GetAgreementsListRequest(accountId))
			.call();
	}

	public static GetAgreementResponse getAgreement(String accountId, String accessToken, String agreementId)
		throws Exception {
		return createIamClient(accessToken)
			.agreementManager()
			.agreements()
			.getAgreement()
			.accountId(accountId)
			.agreementId(agreementId)
			.call();
	}

	public static String serializeObjectToJson(Object data) throws Exception {
		var mapper = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.registerModule(new JavaTimeModule())
			.registerModule(new JsonNullableModule());

		return mapper.writeValueAsString(data);
	}

	public static BulkUploadJobInfo createBulkUploadJob(
		String accountId,
		String accessToken
	) throws Exception {
		var client = createIamClient(accessToken);

		var demoDocs = getAvailableDemoDocuments();
		if (demoDocs.isEmpty()) {
			throw new IllegalStateException("No demo documents found on classpath");
		}

		var createJob = CreateBulkJob.builder()
			.jobName("Example bulk upload job")
			.expectedNumberOfDocs(demoDocs.size())
			.language("en-US")
			.build();

		var createResponse = client.agreementManager().bulkJob()
			.createBulkUploadJob(accountId, createJob);

		var bulkJob = createResponse.bulkJob().orElseThrow();
		var jobId = bulkJob.id();
		if (jobId == null || jobId.isBlank()) {
			throw new IllegalStateException("API returned a bulk job with no ID");
		}

		var documents = bulkJob.embedded().orElseThrow().documents().orElseThrow();

		var uploadUrls = new ArrayList<String>();
		for (var doc : documents) {
			var actions = doc.actions().orElse(null);
			var url = actions != null ? actions.uploadDocument().orElse(null) : null;
			uploadUrls.add(url != null ? url : "");
		}

		return new BulkUploadJobInfo(jobId, uploadUrls);
	}

	public static void uploadDocumentsToJob(List<String> uploadUrls) throws Exception {
		var demoDocs = getAvailableDemoDocuments();

		for (int i = 0; i < demoDocs.size(); i++) {
			if (i >= uploadUrls.size())
				break;

			var uploadUrl = uploadUrls.get(i);
			if (uploadUrl == null || uploadUrl.isEmpty())
				continue;

			var docInfo = demoDocs.get(i);
			var bytes = loadClasspathResource(docInfo[0]);
			if (bytes == null)
				continue;

			uploadToBlobStorage(bytes, docInfo[1], docInfo[2], uploadUrl);
		}
	}

	public static UploadCompleteBulkJobResponse completeBulkUploadJob(
			String accountId,
			String accessToken,
			String jobId
		) throws Exception {
		return createIamClient(accessToken).agreementManager().bulkJob()
			.uploadCompleteBulkJob(accountId, jobId);
	}

	private static List<String[]> getAvailableDemoDocuments() {
		String[][] candidates = {
			{ "World_Wide_Corp_Battle_Plan_Trafalgar.docx",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
				"World_Wide_Corp_Battle_Plan_Trafalgar.docx" },
			{ "World_Wide_Corp_lorem.pdf", "application/pdf", "World_Wide_Corp_lorem.pdf" },
			{ "doc_1.html", "text/html", "doc_1.html" },
			{ "Welcome.txt", "text/plain", "Welcome.txt" },
			{ "Id.jpg", "image/jpeg", "Id.jpg" },
		};

		var available = new ArrayList<String[]>();
		for (var doc : candidates) {
			if (NavigatorMethodsService.class.getClassLoader().getResource(doc[0]) != null) {
				available.add(doc);
			}
		}
		return available;
	}

	private static byte[] loadClasspathResource(String resourcePath) {
		try (var stream = NavigatorMethodsService.class.getClassLoader().getResourceAsStream(resourcePath)) {
			if (stream == null)
				return null;

			return stream.readAllBytes();
		} catch (Exception e) {
			Logger.getLogger(NavigatorMethodsService.class.getName())
					.warning("Could not load resource: " + resourcePath);
			return null;
		}
	}

	private static void uploadToBlobStorage(
		byte[] bytes,
		String contentType,
		String fileName,
		String url
	) throws Exception {
		var request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.header("Content-Type", contentType)
			.header("x-ms-blob-type", "BlockBlob")
			.header("x-ms-meta-filename", fileName)
			.PUT(HttpRequest.BodyPublishers.ofByteArray(bytes))
			.build();
		var httpResponse = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());

		if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
			throw new java.io.IOException("Blob upload failed for " + fileName + ": HTTP " + httpResponse.statusCode());
		}
	}
}
