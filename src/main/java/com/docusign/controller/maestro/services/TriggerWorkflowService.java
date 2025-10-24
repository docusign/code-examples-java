package com.docusign.controller.maestro.services;

import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.TriggerInputs;
import com.docusign.iam.sdk.models.components.TriggerWorkflow;
import com.docusign.iam.sdk.models.operations.*;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TriggerWorkflowService {
    private static final String MaestroBasePath = "https://demo.services.docusign.net/aow-manage/v1.0";

    public static final String CONTENT_TYPE = "application/json";

    public static final String BEARER = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String ACCEPT_HEADER = "Accept";

    public static String createWorkflow(
            String accessToken,
            String accountId,
            String templateId,
            String fileLocation
    ) throws Exception {
        String signerId = UUID.randomUUID().toString();
        String ccId = UUID.randomUUID().toString();
        String triggerId = "wfTrigger";

        String requestJson = prepareWorkflowDefinition(fileLocation, templateId, signerId, ccId, triggerId, accountId);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MaestroBasePath + "/management/accounts/" + accountId + "/workflowDefinitions"))
                .header(AUTHORIZATION_HEADER, BEARER + accessToken)
                .header(ACCEPT_HEADER, CONTENT_TYPE)
                .header("Content-Type", CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        if (response.statusCode() >= 300) {
            throw new Exception("Unable to create a new workflow: " + responseBody);
        }

        JSONObject jsonResponse = new JSONObject(responseBody);
        return jsonResponse.optString("workflowDefinitionId", null);
    }

    public static String publishWorkflow(
            String accessToken,
            String accountId,
            String workflowId
    ) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MaestroBasePath + "/management/accounts/" + accountId + "/workflowDefinitions/"
                        + workflowId + "/publish?isPreRunCheck=true"))
                .header(AUTHORIZATION_HEADER, BEARER + accessToken)
                .header(ACCEPT_HEADER, CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        if (response.statusCode() > 201) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            String message = jsonResponse.optString("message", null);
            String consentUrl = jsonResponse.optString("consentUrl", null);

            if ("Consent required".equals(message) && consentUrl != null && !consentUrl.isEmpty()) {
                return consentUrl;
            }

            throw new Exception("Error publishing workflow. " + message);
        }

        return "";
    }

    private static String prepareWorkflowDefinition(
            String fileLocation,
            String templateId,
            String signerId,
            String ccId,
            String triggerId,
            String accountId) throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource(fileLocation);
            byte[] buffer = StreamUtils.copyToByteArray(resource.getInputStream());

            String workflowDefinition = new String(buffer);
            return workflowDefinition
                    .replace("TEMPLATE_ID", templateId)
                    .replace("ACCOUNT_ID", accountId)
                    .replace("SIGNER_ID", signerId)
                    .replace("CC_ID", ccId)
                    .replace("TRIGGER_ID", triggerId);
        } catch (IOException ex) {
            throw new Exception("An error occurred: " + ex.getMessage(), ex);
        }
    }

    private static TriggerInputs createTriggerInput(String value) {
        return TriggerInputs.of(value);
    }

    public static TriggerWorkflowResponse triggerWorkflowInstance(
            IamClient client,
            String accountId,
            String workflowId,
            String signerEmail,
            String signerName,
            String ccEmail,
            String ccName,
            String instanceName) throws Exception {
    //ds-snippet-start:Maestro1Step4
        Map<String, TriggerInputs> triggerInputs = new HashMap<>();
        triggerInputs.put("signerName", createTriggerInput(signerName));
        triggerInputs.put("signerEmail", createTriggerInput(signerEmail));
        triggerInputs.put("ccName", createTriggerInput(ccName));
        triggerInputs.put("ccEmail", createTriggerInput(ccEmail));
    //ds-snippet-end:Maestro1Step4

    //ds-snippet-start:Maestro1Step5
w = new TriggerWorkflow(instanceName, triggerInputs);

        return client.maestro()
                .workflows()
                .triggerWorkflow(accountId, workflowId, triggerWorkflow);
    }
    //ds-snippet-end:Maestro1Step5

    //ds-snippet-start:Maestro1Step3
    public static GetWorkflowsListResponse getMaestroWorkflow(
            IamClient client,
            String accountId) throws Exception {
        return client.maestro()
                .workflows()
                .getWorkflowsList(accountId, Optional.of(Status.ACTIVE), Optional.empty());
    }
    //ds-snippet-end:Maestro1Step3
}
