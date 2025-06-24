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
import com.docusign.iam.sdk.IamClient;
import com.docusign.iam.sdk.models.components.TabInfo;
import com.docusign.iam.sdk.models.operations.ConnectedFieldsApiGetTabGroupsResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetConnectedFieldsService {

	private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_lorem.pdf";

	private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

	public static EnvelopeSummary signingViaEmail(
			EnvelopesApi envelopesApi,
			String accountId,
			EnvelopeDefinition envelope) throws ApiException {
		return envelopesApi.createEnvelope(accountId, envelope);
	}

	// ds-snippet-start:ConnectedFields1Step3
	public static ConnectedFieldsApiGetTabGroupsResponse getConnectedFieldsTabGroups(String accountId,
			String accessToken) throws Exception {
		return IamClient.builder()
				.accessToken(accessToken)
				.build()
				.connectedFields()
				.tabInfo()
				.getConnectedFieldsTabGroups()
				.accountId(accountId)
				.call();
	}
	// ds-snippet-end:ConnectedFields1Step3

	// ds-snippet-start:ConnectedFields1Step4
	public static EnvelopeDefinition makeEnvelope(String signerEmail, String signerName, TabInfo selectedApp)
			throws Exception {
		String appId = selectedApp.appId();
		var tabLabels = selectedApp.tabs();

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

		List<Text> textTabs = new ArrayList<>();

		if (tabLabels != null) {
			for (var tab : tabLabels) {
				var extensionData = tab.extensionData();

				String connectionKey = "";
				String connectionValue = "";

				var connectionInstances = extensionData.connectionInstances();
				if (connectionInstances.isPresent() && !connectionInstances.get().isEmpty()) {
					var firstInstance = connectionInstances.get().get(0);
					connectionKey = firstInstance.connectionKey();
					connectionValue = firstInstance.connectionValue();
				}

				Text textTab = new Text();
				textTab.setRequireInitialOnSharedChange("false");
				textTab.setRequireAll("false");
				textTab.setName(extensionData.applicationName().orElse("App"));
				textTab.setRequired("false");
				textTab.setLocked("false");
				textTab.setDisableAutoSize("false");
				textTab.setMaxLength("4000");
				textTab.setTabLabel(tab.tabLabel());
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
				extension.setExtensionGroupId(extensionData.extensionGroupId());
				extension.setPublisherName(extensionData.publisherName().orElse(null));
				extension.setApplicationId(appId);
				extension.setApplicationName(extensionData.applicationName().orElse(null));
				extension.setActionName(extensionData.actionName().orElse(null));
				extension.setActionContract(extensionData.actionContract().orElse(null));
				extension.setExtensionName(extensionData.applicationName().orElse(null));
				extension.setExtensionContract(extensionData.extensionContract().orElse(null));
				extension.setRequiredForExtension(String.valueOf(extensionData.requiredForExtension()));
				extension.setActionInputKey(extensionData.actionInputKey());
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
	// ds-snippet-end:ConnectedFields1Step5

	public static List<TabInfo> filterData(ConnectedFieldsApiGetTabGroupsResponse connectedFields) {
		List<TabInfo> filteredList = new ArrayList<>();

		for (var tabInfoGroup : connectedFields.tabInfos().orElse(List.of())) {
			for (var tab : tabInfoGroup.tabs()) {
				var extensionData = tab.extensionData();
				String tabLabel = tab.tabLabel();

				boolean hasVerify = extensionData != null &&
						extensionData.actionContract().isPresent() &&
						extensionData.actionContract().get().contains("Verify");

				boolean hasConnectedData = tabLabel != null && tabLabel.contains("connecteddata");

				if (hasVerify || hasConnectedData) {
					filteredList.add(tabInfoGroup);
					break;
				}
			}
		}

		return filteredList;
	}

	public static TabInfo findAppById(List<TabInfo> extensionApps, String appId) {
		return extensionApps.stream()
				.filter(app -> appId.equals(app.appId()))
				.findFirst()
				.orElse(null);
	}
}
