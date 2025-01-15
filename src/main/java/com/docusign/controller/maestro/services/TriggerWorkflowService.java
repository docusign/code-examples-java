package com.docusign.controller.maestro.services;

import com.docusign.maestro.api.WorkflowManagementApi;
import com.docusign.maestro.api.WorkflowTriggerApi;
import com.docusign.maestro.client.ApiClient;
import com.docusign.maestro.client.ApiException;
import com.docusign.maestro.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class TriggerWorkflowService {
    public static WorkflowDefinitionList getWorkflowDefinitions(
            ApiClient apiClient,
            String accountId
    ) throws ApiException {
        var maestroApi = new WorkflowManagementApi(apiClient);
        var options = maestroApi.new GetWorkflowDefinitionsOptions();
        options.setStatus("active");
        return maestroApi.getWorkflowDefinitions(accountId, options);
    }

    public static WorkflowDefinitionWithId getWorkflowDefinition(
            ApiClient apiClient,
            String accountId,
            String workflowId
    ) throws ApiException {
        var maestroApi = new WorkflowManagementApi(apiClient);
        return maestroApi.getWorkflowDefinition(accountId, workflowId);
    }

    public static String publishWorkFlow(
            ApiClient apiClient,
            String accountId,
            String workflowId
    ) throws ApiException {
        try {
            var maestroApi = new WorkflowManagementApi(apiClient);
            maestroApi.publishOrUnPublishWorkflowDefinition(accountId, workflowId, new DeployRequest());
            return "";
        } catch (ApiException exception) {
            if (exception.getResponseBody().contains("Consent required"))
            {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(exception.getResponseBody(), JsonObject.class);
                return jsonObject.get("consentUrl").getAsString();
            }
            else
            {
                throw exception;
            }
        }
    }

    public static TriggerWorkflowViaPostResponse triggerWorkflow(
            ApiClient apiClient,
            String accountId,
            String triggerUrl,
            String signerEmail,
            String signerName,
            String ccEmail,
            String ccName,
            String instanceName,
            String worlflowId) throws ApiException, URISyntaxException
    {
        var workflowTriggerApi = new WorkflowTriggerApi(apiClient);

        Map<String, String> emailData = new HashMap<>();
        emailData.put("signerEmail", signerEmail);
        emailData.put("signerName", signerName);
        emailData.put("ccEmail", ccEmail);
        emailData.put("ccName", ccName);

        var payload = new TriggerPayload().instanceName(instanceName)
                .metadata(new WorkflowDefinitionMetadata())
                .participants(new ArrayList<Participant>())
                .payload(emailData);

        java.net.URI uri = new java.net.URI(triggerUrl);
        Map<String, String> parameters = parseQueryString(uri.getQuery());
        String mtid = parameters.get("mtid");
        String mtsec = parameters.get("mtsec");

        WorkflowTriggerApi.TriggerWorkflowOptions options = workflowTriggerApi.new TriggerWorkflowOptions();
        options.setMtid(mtid);
        options.setMtsec(mtsec);

        return workflowTriggerApi.triggerWorkflow(accountId, worlflowId, payload, options);
    }

    private static Map<String, String> parseQueryString(String query) {
        Map<String, String> parameters = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return parameters;
    }
}
