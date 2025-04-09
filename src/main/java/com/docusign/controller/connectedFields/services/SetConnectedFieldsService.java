package com.docusign.controller.connectedFields.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.ConnectionInstance;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.ExtensionData;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.Text;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetConnectedFieldsService {
	private static final String ACTION_CONTRACT = "actionContract";

	private static final String TAB_LABEL = "tabLabel";

	private static final String APPLICATION_NAME = "applicationName";

	private static final String EXTENSION_DATA = "extensionData";

	private static final String TABS = "tabs";

	private static final String APP_ID = "appId";

	private static final String EXPECTED_A_JSON_ARRAY = "Expected a JSON array";

	private static final HttpClient client = HttpClient.newHttpClient();

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

	private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

	public static EnvelopeSummary signingViaEmail(
			EnvelopesApi envelopesApi,
			String accountId,
			EnvelopeDefinition envelope) throws ApiException {
		return envelopesApi.createEnvelope(accountId, envelope);
	}

	public static String getConnectedFieldsTabGroups(String accountId, String accessToken) throws Exception {
		String url = String.format(
				"https://api-d.docusign.com/v1/accounts/%s/connected-fields/tab-groups",
				accountId);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.GET()
				.header("Authorization", "Bearer " + accessToken)
				.header("Accept", "application/json")
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() >= 200 && response.statusCode() < 300) {
				return response.body();
			} else {
				throw new IOException("Unexpected response code: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			throw new Exception("DocuSign API Request failed: " + e.getMessage(), e);
		}
	}

	public static EnvelopeDefinition makeEnvelope(String signerEmail, String signerName, JsonNode selectedApp)
			throws Exception {
		String appId = selectedApp.has(APP_ID) ? selectedApp.get(APP_ID).asText() : "";
		JsonNode tabLabels = selectedApp.get(TABS);

		EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
		envelopeDefinition.setEmailSubject("Please sign this document set");
		envelopeDefinition.setStatus("sent");

		Document document = EnvelopeHelpers.createDocumentFromFile(
				PDF_DOCUMENT_FILE_NAME,
				PDF_DOCUMENT_NAME,
				"1");

		envelopeDefinition.setDocuments(Collections.singletonList(document));

		Signer signer = new Signer();
		signer.setEmail(signerEmail);
		signer.setName(signerName);
		signer.setRecipientId("1");
		signer.setRoutingOrder("1");

		SignHere signHere = new SignHere();
		signHere.setAnchorString("/sn1/");
		signHere.setAnchorUnits("pixels");
		signHere.setAnchorYOffset("10");
		signHere.setAnchorXOffset("20");

		List<Text> textTabs = new ArrayList<Text>();

		if (tabLabels != null && tabLabels.isArray()) {
			for (JsonNode tab : tabLabels) {
				JsonNode extensionData = tab.get(EXTENSION_DATA);

				String connectionKey = "";
				String connectionValue = "";

				JsonNode connectionInstances = extensionData != null ? extensionData.get("connectionInstances") : null;
				if (connectionInstances != null && connectionInstances.isArray() && connectionInstances.size() > 0) {
					JsonNode firstInstance = connectionInstances.get(0);
					connectionKey = getText(firstInstance, "connectionKey");
					connectionValue = getText(firstInstance, "connectionValue");
				}

				String extensionGroupId = getText(extensionData, "extensionGroupId");
				String publisherName = getText(extensionData, "publisherName");
				String applicationName = getText(extensionData, APPLICATION_NAME);
				String actionName = getText(extensionData, "actionName");
				String actionInputKey = getText(extensionData, "actionInputKey");
				String actionContract = getText(extensionData, ACTION_CONTRACT);
				String extensionName = getText(extensionData, "extensionName");
				String extensionContract = getText(extensionData, "extensionContract");
				String requiredForExtension = getText(extensionData, "requiredForExtension");
				String tabLabel = getText(tab, TAB_LABEL);

				Text textTab = new Text();
				textTab.setRequireInitialOnSharedChange("false");
				textTab.setRequireAll("false");
				textTab.setName(applicationName);
				textTab.setRequired("false");
				textTab.setLocked("false");
				textTab.setDisableAutoSize("false");
				textTab.setMaxLength("4000");
				textTab.setTabLabel(tabLabel);
				textTab.setFont("lucidaconsole");
				textTab.setFontColor("black");
				textTab.setFontSize("size9");
				textTab.setDocumentId("1");
				textTab.setRecipientId("1");
				textTab.setPageNumber("1");
				textTab.setXPosition("273");
				textTab.setYPosition("191");
				textTab.setWidth("84");
				textTab.setHeight("22");
				textTab.setTemplateRequired("false");
				textTab.setTabType("text");

				ExtensionData extension = new ExtensionData();
				extension.setExtensionGroupId(extensionGroupId);
				extension.setPublisherName(publisherName);
				extension.setApplicationId(appId);
				extension.setApplicationName(applicationName);
				extension.setActionName(actionName);
				extension.setActionContract(actionContract);
				extension.setExtensionName(extensionName);
				extension.setExtensionContract(extensionContract);
				extension.setRequiredForExtension(requiredForExtension);
				extension.setActionInputKey(actionInputKey);
				extension.setExtensionPolicy("None");

				ConnectionInstance connectionInstance = new ConnectionInstance();
				connectionInstance.setConnectionKey(connectionKey);
				connectionInstance.setConnectionValue(connectionValue);
				extension.setConnectionInstances(Collections.singletonList(connectionInstance));

				textTab.setExtensionData(extension);
				textTabs.add(textTab);
			}
		}

		Tabs tabs = new Tabs();
		tabs.setSignHereTabs(Collections.singletonList(signHere));
		tabs.setTextTabs(textTabs);
		signer.setTabs(tabs);

		Recipients recipients = new Recipients();
		recipients.setSigners(Collections.singletonList(signer));
		envelopeDefinition.setRecipients(recipients);

		return envelopeDefinition;
	}

	public static List<Map<String, String>> convertJsonToList(String jsonString) throws Exception {
		JsonNode root = objectMapper.readTree(jsonString);
		List<Map<String, String>> result = new ArrayList<>();

		if (root.isArray()) {
			for (JsonNode app : root) {
				String appId = app.has(APP_ID) ? app.get(APP_ID).asText() : null;
				String applicationName = "";

				JsonNode tabs = app.get(TABS);
				if (tabs != null && tabs.isArray() && tabs.size() > 0) {
					JsonNode extensionData = tabs.get(0).get(EXTENSION_DATA);
					if (extensionData != null && extensionData.has(APPLICATION_NAME)) {
						applicationName = extensionData.get(APPLICATION_NAME).asText();
					}
				}

				if (appId != null && applicationName != null) {
					Map<String, String> map = new HashMap<>();
					map.put(APP_ID, appId);
					map.put(APPLICATION_NAME, applicationName);
					result.add(map);
				}
			}
		}

		return result;
	}

	private static String getText(JsonNode node, String fieldName) {
		return node != null && node.has(fieldName) ? node.get(fieldName).asText() : "";
	}

	public static String filterData(String jsonData) throws Exception {
		JsonNode rootNode = objectMapper.readTree(jsonData);

		if (!rootNode.isArray()) {
			throw new IllegalArgumentException(EXPECTED_A_JSON_ARRAY);
		}

		ArrayNode filteredArray = JsonNodeFactory.instance.arrayNode();

		for (JsonNode item : rootNode) {
			JsonNode tabs = item.get(TABS);

			if (tabs != null && tabs.isArray()) {
				for (JsonNode tab : tabs) {
					JsonNode extensionData = tab.get(EXTENSION_DATA);
					String tabLabel = tab.has(TAB_LABEL) ? tab.get(TAB_LABEL).asText() : null;

					boolean hasVerify = extensionData != null &&
							extensionData.has(ACTION_CONTRACT) &&
							extensionData.get(ACTION_CONTRACT).asText().contains("Verify");

					boolean hasConnectedData = tabLabel != null && tabLabel.contains("connecteddata");

					if (hasVerify || hasConnectedData) {
						filteredArray.add(item);
						break;
					}
				}
			}
		}

		return objectMapper.writeValueAsString(filteredArray);
	}

	public static JsonNode findAppById(String extensionAppsJson, String appId) throws Exception {
		JsonNode root = objectMapper.readTree(extensionAppsJson);
		if (!root.isArray()) {
			throw new IllegalArgumentException(EXPECTED_A_JSON_ARRAY);
		}

		for (JsonNode app : root) {
			JsonNode appIdNode = app.get(APP_ID);
			if (appIdNode != null && appId.equals(appIdNode.asText())) {
				return app;
			}
		}

		return null;
	}
}
