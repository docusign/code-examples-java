package com.docusign.controller.maestro.services;

import com.docusign.maestro.api.WorkflowManagementApi;
import com.docusign.maestro.client.ApiException;
import com.docusign.maestro.model.*;

import java.util.*;

public final class CreateWorkflowService {
    private static String versions = "1.0.0";

    public static NewOrUpdatedWorkflowDefinitionResponse createWorkflowDefinition(
            WorkflowManagementApi maestroApi,
            String accountId,
            String templateId
    ) throws ApiException {
        UUID signerId = UUID.randomUUID();
        UUID ccId = UUID.randomUUID();
        String triggerId = "wfTrigger";

        var requestBody = new CreateOrUpdateWorkflowDefinitionRequestBody();
        requestBody.setWorkflowDefinition(CreateModel(accountId, signerId, ccId, triggerId, templateId));

        return maestroApi.createWorkflowDefinition(accountId, requestBody);
    }

    private static WorkflowDefinition CreateModel(
            String accountId,
            UUID signerId,
            UUID ccId,
            String triggerId,
            String templateId
    ) {
        WorkflowDefinition workflowDefinition = new WorkflowDefinition();

        workflowDefinition.setWorkflowName("Example workflow - send invite to signer");
        workflowDefinition.setWorkflowDescription("");
        workflowDefinition.setAccountId(accountId);
        workflowDefinition.setDocumentVersion(versions);
        workflowDefinition.setSchemaVersion(versions);
        workflowDefinition.setParticipants(PrepareParticipants(signerId, ccId));
        workflowDefinition.setTrigger(PrepareTrigger(triggerId));
        workflowDefinition.setVariables(PrepareVariables(triggerId));
        workflowDefinition.setSteps(PrepareSteps(templateId, signerId, triggerId));

        return workflowDefinition;
    }

    private static Map<String, Participant> PrepareParticipants(UUID signerId, UUID ccId) {
        Map<String, Participant> participants = new HashMap<>();

        participants.put(signerId.toString(), prepareParticipant("Signer"));
        participants.put(ccId.toString(), prepareParticipant("CC"));

        return participants;
    }

    private static Participant prepareParticipant(String role) {
        Participant participant = new Participant();
        participant.setParticipantRole(role);
        return participant;
    }

    private static DSWorkflowTrigger PrepareTrigger(String triggerId) {
        DSWorkflowTrigger trigger = new DSWorkflowTrigger();
        trigger.setName("Get_URL");
        trigger.setType(DSWorkflowTriggerTypes.HTTP);
        trigger.setHttpType(HttpTypes.GET);
        trigger.setId(triggerId);

        LinkedHashMap<String, Object> input = new LinkedHashMap<>();
        LinkedHashMap<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("customAttributes", new LinkedHashMap<>());
        input.put("metadata", metadata);

        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put(
                "dacId_" + triggerId,
                prepareProperty("step", "dacId", triggerId));
        payload.put(
                "id_" + triggerId,
                prepareProperty("step", "id", triggerId));
        payload.put(
                "signerName_" + triggerId,
                prepareProperty("step", "signerName", triggerId));
        payload.put(
                "signerEmail_" + triggerId,
                prepareProperty("step", "signerEmail", triggerId));
        payload.put(
                "ccName_" + triggerId,
                prepareProperty("step", "ccName", triggerId));
        payload.put(
                "ccEmail_" + triggerId,
                prepareProperty("step", "ccEmail", triggerId));
        input.put("payload", payload);

        input.put("participants", new LinkedHashMap<>());
        trigger.setInput(input);

        Map<String, Object> output = new HashMap<>();
        output.put("dacId_" + triggerId, prepareProperty("step", "dacId", triggerId));
        trigger.setOutput(output);

        return trigger;
    }

    private static LinkedHashMap<String, Object> prepareProperty(String source, String propertyName, String stepId) {
        LinkedHashMap<String, Object> property = new LinkedHashMap<>();
        property.put("source", source);
        property.put("propertyName", propertyName);
        property.put("stepId", stepId);
        return property;
    }

    private static Map<String, Object> PrepareVariables(String triggerId) {
        Map<String, Object> variables = new HashMap<>();

        variables.put(
                "dacId_" + triggerId,
                prepareProperty("step", "dacId", triggerId));
        variables.put(
                "id_" + triggerId,
                prepareProperty("step", "id", triggerId));
        variables.put(
                "signerName_" + triggerId,
                prepareProperty("step", "signerName", triggerId));
        variables.put(
                "signerEmail_" + triggerId,
                prepareProperty("step", "signerEmail", triggerId));
        variables.put(
                "ccName_" + triggerId,
                prepareProperty("step", "ccName", triggerId));
        variables.put(
                "ccEmail_" + triggerId,
                prepareProperty("step", "ccEmail", triggerId));
        variables.put(
                "envelopeId_step2",
                prepareProperty("step", "envelopeId", "step2", "String"));
        variables.put(
                "combinedDocumentsBase64_step2",
                prepareProperty("step", "combinedDocumentsBase64", "step2", "File"));
        variables.put(
                "fields.signer.text.value_step2",
                prepareProperty("step", "fields.signer.text.value", "step2", "String"));

        return variables;
    }

    private static List<Object> PrepareSteps(String templateId, UUID signerId, String triggerId) {
        List<Object> steps = new ArrayList<>();
        steps.add(StepOneSetUpInvite(signerId, triggerId));
        steps.add(StepTwoGetSignatures(templateId, signerId, triggerId));
        steps.add(StepThreeConfirm(signerId));
        return steps;
    }

    private static LinkedHashMap<String, Object> StepOneSetUpInvite(UUID signerId, String triggerId) {
        LinkedHashMap<String, Object> stepOne = new LinkedHashMap<>();
        stepOne.put("id", "step1");
        stepOne.put("name", "Set Up Invite");
        stepOne.put("moduleName", "Notification-SendEmail");
        stepOne.put("configurationProgress", "Completed");
        stepOne.put("type", "DS-EmailNotification");

        LinkedHashMap<String, Object> config = new LinkedHashMap<>();
        config.put("templateType", "WorkflowParticipantNotification");
        config.put("templateVersion", 1);
        config.put("language", "en");
        config.put("sender_name", "DocuSign Orchestration");
        config.put("sender_alias", "Orchestration");
        config.put("participantId", signerId.toString());

        LinkedHashMap<String, Object> input = new LinkedHashMap<>();
        LinkedHashMap<String, Object> recipient = new LinkedHashMap<>();
        recipient.put("name", prepareProperty("step", "signerName", triggerId));
        recipient.put("email", prepareProperty("step", "signerEmail", triggerId));
        input.put("recipients", Collections.singletonList(recipient));

        LinkedHashMap<String, Object> mergeValues = new LinkedHashMap<>();
        mergeValues.put("CustomMessage", "Follow this link to access and complete the workflow.");
        mergeValues.put("ParticipantFullName", prepareProperty("step", "signerName", triggerId));
        input.put("mergeValues", mergeValues);

        stepOne.put("config", config);
        stepOne.put("input", input);
        stepOne.put("output", new LinkedHashMap<>());

        return stepOne;
    }

    private static Map<String, Object> StepTwoGetSignatures(String templateId, UUID signerId, String triggerId) {
        Map<String, Object> stepTwo = new HashMap<>();
        stepTwo.put("id", "step2");
        stepTwo.put("name", "Get Signatures");
        stepTwo.put("moduleName", "ESign");
        stepTwo.put("configurationProgress", "Completed");
        stepTwo.put("type", "DS-Sign");

        Map<String, Object> config = new HashMap<>();
        config.put("participantId", signerId.toString());
        stepTwo.put("config", config);

        Map<String, Object> input = new HashMap<>();
        input.put("isEmbeddedSign", true);

        List<Map<String, Object>> documents = new ArrayList<>();
        Map<String, Object> document = new HashMap<>();
        document.put("type", "FromDSTemplate");
        document.put("eSignTemplateId", templateId);
        documents.add(document);

        input.put("documents", documents);
        input.put("emailSubject", "Please sign this document");
        input.put("emailBlurb", "");

        Map<String, Object> recipients = new HashMap<>();
        recipients.put("signers", PrepareSigners(triggerId));
        recipients.put("carbonCopies", PrepareCC(triggerId));
        input.put("recipients", recipients);
        stepTwo.put("input", input);

        Map<String, Object> output = new HashMap<>();
        output.put(
                "envelopeId_step2",
                prepareProperty("step", "envelopeId", "step2", "String")
        );
        output.put(
                "combinedDocumentsBase64_step2",
                prepareProperty("step", "combinedDocumentsBase64", "step2", "File")
        );
        output.put(
                "fields.signer.text.value_step2",
                prepareProperty("step", "fields.signer.text.value", "step2", "String")
        );

        stepTwo.put("output", output);

        return stepTwo;
    }

    private static Map<String, Object> prepareProperty(String source, String propertyName, String stepId, String type) {
        Map<String, Object> outputMap = new HashMap<>();
        outputMap.put("source", source);
        outputMap.put("propertyName", propertyName);
        outputMap.put("stepId", stepId);
        outputMap.put("type", type);
        return outputMap;
    }

    private static List<Map<String, Object>> PrepareSigners(String triggerId) {
        List<Map<String, Object>> signers = new ArrayList<>();

        Map<String, Object> signer = new HashMap<>();
        signer.put("defaultRecipient", "false");

        Map<String, Object> tabs = new HashMap<>();
        tabs.put("numericalTabs", prepareNumericalTabs());
        tabs.put("listTabs", prepareListTabs());
        tabs.put("radioGroupTabs", prepareRadioGroupTabs());
        tabs.put("checkboxTabs", prepareCheckboxTabs());
        tabs.put("signHereTabs", Collections.singletonList(prepareSignHereTabs()));
        tabs.put("textTabs", Collections.singletonList(prepareTextTabs()));

        signer.put("tabs", tabs);

        signer.put("signInEachLocation", "false");
        signer.put("agentCanEditEmail", "false");
        signer.put("agentCanEditName", "false");
        signer.put("requireUploadSignature", "false");

        signer.put("name", prepareProperty("step", "signerName", triggerId));
        signer.put("email", prepareProperty("step", "signerEmail", triggerId));

        signer.put("recipientId", "1");
        signer.put("recipientIdGuid", "00000000-0000-0000-0000-000000000000");
        signer.put("accessCode", "");
        signer.put("requireIdLookup", "false");
        signer.put("routingOrder", "1");
        signer.put("note", "");
        signer.put("roleName", "signer");
        signer.put("completedCount", "0");
        signer.put("deliveryMethod", "email");
        signer.put("templateLocked", "false");
        signer.put("templateRequired", "false");
        signer.put("inheritEmailNotificationConfiguration", "false");
        signer.put("recipientType", "signer");

        signers.add(signer);

        return signers;
    }

    private static List<Map<String, Object>> prepareRadioGroupTabs() {
        List<Map<String, Object>> radioGroupTabs = new ArrayList<>();

        Map<String, Object> radioGroupTab = new HashMap<>();
        radioGroupTab.put("documentId", "1");
        radioGroupTab.put("recipientId", "1");
        radioGroupTab.put("groupName", "radio1");

        List<Map<String, Object>> radios = new ArrayList<>();

        Map<String, Object> radio1 = new HashMap<>();
        radio1.put("pageNumber", "1");
        radio1.put("xPosition", "142");
        radio1.put("yPosition", "384");
        radio1.put("value", "white");
        radio1.put("selected", "false");
        radio1.put("tabId", "7");
        radio1.put("required", "false");
        radio1.put("locked", "false");
        radio1.put("bold", "false");
        radio1.put("italic", "false");
        radio1.put("underline", "false");
        radio1.put("fontColor", "black");
        radio1.put("fontSize", "size7");

        Map<String, Object> radio2 = new HashMap<>();
        radio2.put("pageNumber", "1");
        radio2.put("xPosition", "74");
        radio2.put("yPosition", "384");
        radio2.put("value", "red");
        radio2.put("selected", "false");
        radio2.put("tabId", "8");
        radio2.put("required", "false");
        radio2.put("locked", "false");
        radio2.put("bold", "false");
        radio2.put("italic", "false");
        radio2.put("underline", "false");
        radio2.put("fontColor", "black");
        radio2.put("fontSize", "size7");

        Map<String, Object> radio3 = new HashMap<>();
        radio3.put("pageNumber", "1");
        radio3.put("xPosition", "220");
        radio3.put("yPosition", "384");
        radio3.put("value", "blue");
        radio3.put("selected", "false");
        radio3.put("tabId", "9");
        radio3.put("required", "false");
        radio3.put("locked", "false");
        radio3.put("bold", "false");
        radio3.put("italic", "false");
        radio3.put("underline", "false");
        radio3.put("fontColor", "black");
        radio3.put("fontSize", "size7");

        radios.add(radio1);
        radios.add(radio2);
        radios.add(radio3);

        radioGroupTab.put("radios", radios);
        radioGroupTab.put("shared", "false");
        radioGroupTab.put("requireInitialOnSharedChange", "false");
        radioGroupTab.put("requireAll", "false");
        radioGroupTab.put("tabType", "radiogroup");
        radioGroupTab.put("value", "");
        radioGroupTab.put("originalValue", "");

        radioGroupTabs.add(radioGroupTab);
        return radioGroupTabs;
    }

    private static List<Map<String, Object>> prepareListTabs() {
        List<Map<String, Object>> listTabs = new ArrayList<>();

        Map<String, Object> listTab = new HashMap<>();
        listTab.put("listItems", Arrays.asList(
                Map.of("text", "Red", "value", "red", "selected", "false"),
                Map.of("text", "Orange", "value", "orange", "selected", "false"),
                Map.of("text", "Yellow", "value", "yellow", "selected", "false"),
                Map.of("text", "Green", "value", "green", "selected", "false"),
                Map.of("text", "Blue", "value", "blue", "selected", "false"),
                Map.of("text", "Indigo", "value", "indigo", "selected", "false"),
                Map.of("text", "Violet", "value", "violet", "selected", "false")
        ));
        listTab.put("value", "");
        listTab.put("originalValue", "");
        listTab.put("required", "false");
        listTab.put("locked", "false");
        listTab.put("requireAll", "false");
        listTab.put("tabLabel", "list");
        listTab.put("font", "helvetica");
        listTab.put("fontSize", "size14");
        listTab.put("localePolicy", new HashMap<>());
        listTab.put("documentId", "1");
        listTab.put("recipientId", "1");
        listTab.put("pageNumber", "1");
        listTab.put("xPosition", "142");
        listTab.put("yPosition", "291");
        listTab.put("width", "78");
        listTab.put("height", "0");
        listTab.put("tabId", "10");
        listTab.put("tabType", "list");

        listTabs.add(listTab);
        return listTabs;
    }

    private static List<Map<String, Object>> prepareNumericalTabs() {
        List<Map<String, Object>> numericalTabs = new ArrayList<>();

        Map<String, Object> numericalTab = new HashMap<>();
        numericalTab.put("validationType", "currency");
        numericalTab.put("value", "");
        numericalTab.put("required", "false");
        numericalTab.put("locked", "false");
        numericalTab.put("concealValueOnDocument", "false");
        numericalTab.put("disableAutoSize", "false");
        numericalTab.put("tabLabel", "numericalCurrency");
        numericalTab.put("font", "helvetica");
        numericalTab.put("fontSize", "size14");

        Map<String, Object> localePolicy = new HashMap<>();
        localePolicy.put("cultureName", "en-US");
        localePolicy.put("currencyPositiveFormat", "csym_1_comma_234_comma_567_period_89");
        localePolicy.put("currencyNegativeFormat", "opar_csym_1_comma_234_comma_567_period_89_cpar");
        localePolicy.put("currencyCode", "usd");

        numericalTab.put("localePolicy", localePolicy);
        numericalTab.put("documentId", "1");
        numericalTab.put("recipientId", "1");
        numericalTab.put("pageNumber", "1");
        numericalTab.put("xPosition", "163");
        numericalTab.put("yPosition", "260");
        numericalTab.put("width", "84");
        numericalTab.put("height", "0");
        numericalTab.put("tabId", "11");
        numericalTab.put("tabType", "numerical");

        numericalTabs.add(numericalTab);
        return numericalTabs;
    }

    private static List<Map<String, Object>> prepareCheckboxTabs() {
        List<Map<String, Object>> checkboxTabs = new ArrayList<>();

        Map<String, Object> checkboxTab1 = new HashMap<>();
        checkboxTab1.put("name", "");
        checkboxTab1.put("tabLabel", "ckAuthorization");
        checkboxTab1.put("selected", "false");
        checkboxTab1.put("selectedOriginal", "false");
        checkboxTab1.put("requireInitialOnSharedChange", "false");
        checkboxTab1.put("required", "true");
        checkboxTab1.put("locked", "false");
        checkboxTab1.put("documentId", "1");
        checkboxTab1.put("recipientId", "1");
        checkboxTab1.put("pageNumber", "1");
        checkboxTab1.put("xPosition", "75");
        checkboxTab1.put("yPosition", "417");
        checkboxTab1.put("width", "0");
        checkboxTab1.put("height", "0");
        checkboxTab1.put("tabId", "3");
        checkboxTab1.put("tabType", "checkbox");

        Map<String, Object> checkboxTab2 = new HashMap<>();
        checkboxTab2.put("name", "");
        checkboxTab2.put("tabLabel", "ckAuthentication");
        checkboxTab2.put("selected", "false");
        checkboxTab2.put("selectedOriginal", "false");
        checkboxTab2.put("requireInitialOnSharedChange", "false");
        checkboxTab2.put("required", "true");
        checkboxTab2.put("locked", "false");
        checkboxTab2.put("documentId", "1");
        checkboxTab2.put("recipientId", "1");
        checkboxTab2.put("pageNumber", "1");
        checkboxTab2.put("xPosition", "75");
        checkboxTab2.put("yPosition", "447");
        checkboxTab2.put("width", "0");
        checkboxTab2.put("height", "0");
        checkboxTab2.put("tabId", "4");
        checkboxTab2.put("tabType", "checkbox");

        Map<String, Object> checkboxTab3 = new HashMap<>();
        checkboxTab3.put("name", "");
        checkboxTab3.put("tabLabel", "ckAgreement");
        checkboxTab3.put("selected", "false");
        checkboxTab3.put("selectedOriginal", "false");
        checkboxTab3.put("requireInitialOnSharedChange", "false");
        checkboxTab3.put("required", "true");
        checkboxTab3.put("locked", "false");
        checkboxTab3.put("documentId", "1");
        checkboxTab3.put("recipientId", "1");
        checkboxTab3.put("pageNumber", "1");
        checkboxTab3.put("xPosition", "75");
        checkboxTab3.put("yPosition", "478");
        checkboxTab3.put("width", "0");
        checkboxTab3.put("height", "0");
        checkboxTab3.put("tabId", "5");
        checkboxTab3.put("tabType", "checkbox");

        Map<String, Object> checkboxTab4 = new HashMap<>();
        checkboxTab4.put("name", "");
        checkboxTab4.put("tabLabel", "ckAcknowledgement");
        checkboxTab4.put("selected", "false");
        checkboxTab4.put("selectedOriginal", "false");
        checkboxTab4.put("requireInitialOnSharedChange", "false");
        checkboxTab4.put("required", "true");
        checkboxTab4.put("locked", "false");
        checkboxTab4.put("documentId", "1");
        checkboxTab4.put("recipientId", "1");
        checkboxTab4.put("pageNumber", "1");
        checkboxTab4.put("xPosition", "75");
        checkboxTab4.put("yPosition", "508");
        checkboxTab4.put("width", "0");
        checkboxTab4.put("height", "0");
        checkboxTab4.put("tabId", "6");
        checkboxTab4.put("tabType", "checkbox");

        checkboxTabs.add(checkboxTab1);
        checkboxTabs.add(checkboxTab2);
        checkboxTabs.add(checkboxTab3);
        checkboxTabs.add(checkboxTab4);
        return checkboxTabs;
    }

    private static Map<String, Object> prepareTextTabs() {
        Map<String, Object> textTabs = new HashMap<>();
        textTabs.put("requireAll", "false");
        textTabs.put("value", "");
        textTabs.put("required", "false");
        textTabs.put("locked", "false");
        textTabs.put("concealValueOnDocument", "false");
        textTabs.put("disableAutoSize", "false");
        textTabs.put("tabLabel", "text");
        textTabs.put("font", "helvetica");
        textTabs.put("fontSize", "size14");
        textTabs.put("localePolicy", new HashMap<>());
        textTabs.put("documentId", "1");
        textTabs.put("recipientId", "1");
        textTabs.put("pageNumber", "1");
        textTabs.put("xPosition", "153");
        textTabs.put("yPosition", "230");
        textTabs.put("width", "84");
        textTabs.put("height", "23");
        textTabs.put("tabId", "2");
        textTabs.put("tabType", "text");
        return textTabs;
    }

    private static Map<String, Object> prepareSignHereTabs() {
        Map<String, Object> signHereTabs = new HashMap<>();
        signHereTabs.put("stampType", "signature");
        signHereTabs.put("name", "SignHere");
        signHereTabs.put("tabLabel", "Sign Here");
        signHereTabs.put("scaleValue", "1");
        signHereTabs.put("optional", "false");
        signHereTabs.put("documentId", "1");
        signHereTabs.put("recipientId", "1");
        signHereTabs.put("pageNumber", "1");
        signHereTabs.put("xPosition", "191");
        signHereTabs.put("yPosition", "148");
        signHereTabs.put("tabId", "1");
        signHereTabs.put("tabType", "signhere");
        return signHereTabs;
    }

    private static List<Map<String, Object>> PrepareCC(String triggerId) {
        List<Map<String, Object>> carbonCopies = new ArrayList<>();
        Map<String, Object> carbonCopy = new HashMap<>();
        carbonCopy.put("agentCanEditEmail", "false");
        carbonCopy.put("agentCanEditName", "false");

        carbonCopy.put("name", prepareProperty("step", "ccName", triggerId));
        carbonCopy.put("email", prepareProperty("step", "ccEmail", triggerId));

        carbonCopy.put("recipientId", "2");
        carbonCopy.put("recipientIdGuid", "00000000-0000-0000-0000-000000000000");
        carbonCopy.put("accessCode", "");
        carbonCopy.put("requireIdLookup", "false");
        carbonCopy.put("routingOrder", "2");
        carbonCopy.put("note", "");
        carbonCopy.put("roleName", "cc");
        carbonCopy.put("completedCount", "0");
        carbonCopy.put("deliveryMethod", "email");
        carbonCopy.put("templateLocked", "false");
        carbonCopy.put("templateRequired", "false");
        carbonCopy.put("inheritEmailNotificationConfiguration", "false");
        carbonCopy.put("recipientType", "carboncopy");
        carbonCopies.add(carbonCopy);

        return carbonCopies;
    }

    private static LinkedHashMap<String, Object> StepThreeConfirm(UUID signerId) {
        LinkedHashMap<String, Object> stepThree = new LinkedHashMap<>();
        stepThree.put("id", "step3");
        stepThree.put("name", "Show a Confirmation Screen");
        stepThree.put("moduleName", "ShowConfirmationScreen");
        stepThree.put("configurationProgress", "Completed");
        stepThree.put("type", "DS-ShowScreenStep");

        LinkedHashMap<String, Object> config = new LinkedHashMap<>();
        config.put("participantId", signerId.toString());

        LinkedHashMap<String, Object> input = new LinkedHashMap<>();
        input.put("httpType", "Post");

        LinkedHashMap<String, Object> payload = new LinkedHashMap<>();
        payload.put("participantId", signerId.toString());

        LinkedHashMap<String, Object> confirmationMessage = new LinkedHashMap<>();
        confirmationMessage.put("title", "Tasks complete");
        confirmationMessage.put("description", "You have completed all your workflow tasks.");

        payload.put("confirmationMessage", confirmationMessage);
        input.put("payload", payload);

        stepThree.put("config", config);
        stepThree.put("input", input);
        stepThree.put("output", new LinkedHashMap<>());

        return stepThree;
    }
}
