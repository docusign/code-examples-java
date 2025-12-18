package com.docusign.controller.webForms.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.model.*;
import com.docusign.webforms.api.FormInstanceManagementApi;
import com.docusign.webforms.api.FormManagementApi;
import com.docusign.webforms.client.ApiClient;
import com.docusign.webforms.client.ApiException;
import com.docusign.webforms.client.ApiResponse;
import com.docusign.webforms.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public final class CreateRemoteInstanceService {

    private static final String ROLE_SIGNER = "signer";

    private static final String DOCUMENT_ID = "1";

    private static final String UNITS = "pixels";

    public static WebFormSummaryList getFormsByName(
            ApiClient apiClient,
            String userAccessToken,
            String name) throws ApiException {
        //ds-snippet-start:WebForms2Step3
        FormManagementApi formManagementApi = new FormManagementApi(apiClient);
        var option = formManagementApi.new ListFormsOptions();
        option.setSearch(name);

        ApiResponse<WebFormSummaryList> response = formManagementApi.listFormsWithHttpInfo(userAccessToken, option);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        //ds-snippet-end:WebForms2Step3
    }

    public static void addTemplateIdToForm(String fileName, String templateId) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            byte[] buffer = StreamUtils.copyToByteArray(resource.getInputStream());

            String fileContent = new String(buffer);
            String modifiedContent = fileContent.replace("template-id", templateId);

            Path basePath = Paths.get(System.getProperty("user.dir"), "demo_documents");
            Files.createDirectories(basePath);
            Files.writeString(basePath.resolve(fileName), modifiedContent);
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static WebFormInstance createInstance(
            ApiClient apiClient,
            String accountId,
            String formId,
            String signerEmail,
            String signerName) throws ApiException {
        //ds-snippet-start:WebForms2Step4
        WebFormValues formValues = new WebFormValues();
        formValues.putAll(Map.of(
                "PhoneNumber", "555-555-5555",
                "Yes", new String[] { "Yes" },
                "Company", "Tally",
                "JobTitle", "Programmer Writer"));

        var recipient = new CreateInstanceRequestBodyRecipients()
                .email(signerEmail)
                .name(signerName)
                .roleName(ROLE_SIGNER);

        CreateInstanceRequestBody options = new CreateInstanceRequestBody()
                .sendOption(SendOption.NOW)
                .formValues(formValues)
                .recipients(List.of(recipient));
        //ds-snippet-end:WebForms2Step4

        //ds-snippet-start:WebForms2Step5
        ApiResponse<WebFormInstance> response = new FormInstanceManagementApi(apiClient).createInstanceWithHttpInfo(accountId, formId, options);

        Map<String, List<String>> headers = response.getHeaders();
        List<String> remaining = headers.get("X-RateLimit-Remaining");
        List<String> reset = headers.get("X-RateLimit-Reset");
        
        if (remaining != null & reset != null & !remaining.isEmpty() & !reset.isEmpty()) {
            Instant resetInstant = Instant.ofEpochSecond(Long.parseLong(reset.get(0)));
            System.out.println("API calls remaining: " + remaining);
            System.out.println("Next Reset: " + resetInstant);
        }

        return response.getData();
        //ds-snippet-end:WebForms2Step5
    }

    public static EnvelopeTemplate prepareEnvelopeTemplate(String templateName, String documentPdf) throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(documentPdf, "World_Wide_Web_Form", DOCUMENT_ID);
        Signer signer = createSigner();
        Recipients recipients = new Recipients().signers(List.of(signer));

        return new EnvelopeTemplate()
                .description("Example template created via the API")
                .name(templateName)
                .shared("false")
                .documents(List.of(document))
                .emailSubject("Please sign this document")
                .recipients(recipients)
                .status("created");
    }

    private static Signer createSigner() {
        Signer signer = new Signer()
                .roleName(ROLE_SIGNER)
                .recipientId(DOCUMENT_ID)
                .routingOrder("1");

        signer.tabs(new Tabs()
                .checkboxTabs(List.of(
                        new Checkbox()
                                .documentId(DOCUMENT_ID)
                                .tabLabel("Yes")
                                .anchorString("/SMS/")
                                .anchorUnits(UNITS)
                                .anchorXOffset("0")
                                .anchorYOffset("0")))
                .signHereTabs(List.of(
                        new SignHere()
                                .documentId(DOCUMENT_ID)
                                .tabLabel("Signature")
                                .anchorString("/SignHere/")
                                .anchorUnits(UNITS)
                                .anchorXOffset("20")
                                .anchorYOffset("10")))
                .textTabs(List.of(
                        createText("FullName", "/FullName/"),
                        createText("PhoneNumber", "/PhoneNumber/"),
                        createText("Company", "/Company/"),
                        createText("JobTitle", "/Title/")))
                .dateSignedTabs(List.of(
                        new DateSigned()
                                .documentId(DOCUMENT_ID)
                                .tabLabel("DateSigned")
                                .anchorString("/Date/")
                                .anchorUnits(UNITS)
                                .anchorXOffset("0")
                                .anchorYOffset("0"))));
        return signer;
    }

    private static Text createText(String tabLabel, String anchor) {
        return new Text()
                .documentId(DOCUMENT_ID)
                .tabLabel(tabLabel)
                .anchorString(anchor)
                .anchorUnits(UNITS)
                .anchorXOffset("0")
                .anchorYOffset("0");
    }
}
