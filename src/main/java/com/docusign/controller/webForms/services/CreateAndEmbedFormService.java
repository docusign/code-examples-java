package com.docusign.controller.webForms.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.model.*;
import com.docusign.webforms.api.FormInstanceManagementApi;
import com.docusign.webforms.api.FormManagementApi;
import com.docusign.webforms.client.ApiClient;
import com.docusign.webforms.client.ApiException;
import com.docusign.webforms.model.CreateInstanceRequestBody;
import com.docusign.webforms.model.WebFormInstance;
import com.docusign.webforms.model.WebFormSummaryList;
import com.docusign.webforms.model.WebFormValues;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CreateAndEmbedFormService {
    public static WebFormSummaryList getForms(
        ApiClient apiClient,
        String userAccessToken,
        String search
    ) throws ApiException {
        FormManagementApi formManagementApi = new FormManagementApi(apiClient);
        var option = formManagementApi.new ListFormsOptions();
        option.setSearch(search);

        return formManagementApi.listForms(userAccessToken, option);
    }

    public static void addTemplateIdToForm(String fileName, String templateId) {
        String targetString = "template-id";

        try {
            ClassLoader rawPath = Thread.currentThread().getContextClassLoader();
            String rootDir = Paths.get(Objects.requireNonNull(rawPath.getResource("")).toURI()).toString();
            rootDir = StringUtils.remove(rootDir, "target\\classes");
            rootDir = StringUtils.remove(rootDir, "file:/");
            rootDir = StringUtils.replace(rootDir, "\\", "/");

            String saveFilePath = rootDir + "src/main/resources/" + fileName;

            String fileContent = new String(Files.readAllBytes(Paths.get(saveFilePath)));
            String modifiedContent = fileContent.replace(targetString, templateId);

            Files.write(Paths.get(saveFilePath), modifiedContent.getBytes());
        } catch (IOException | URISyntaxException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    public static WebFormInstance createInstance(
        ApiClient apiClient,
        String accountId,
        String formId
    ) throws ApiException {
        String clientUserId = "1234-5678-abcd-ijkl";

        FormInstanceManagementApi formManagementApi = new FormInstanceManagementApi(apiClient);
        WebFormValues formValues = new WebFormValues();

        formValues.putAll(Map.of(
            "PhoneNumber", "555-555-5555",
            "Yes", new String[]{ "Yes" },
            "Company", "Tally",
            "JobTitle", "Programmer Writer"
        ));

        CreateInstanceRequestBody options = new CreateInstanceRequestBody()
            .clientUserId(clientUserId)
            .formValues(formValues)
            .expirationOffset(3600);
            
        return formManagementApi.createInstance(accountId, formId, options);
    }

    public static EnvelopeTemplate prepareEnvelopeTemplate(String templateName, String documentPdf) throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(
                documentPdf,
                "World_Wide_Web_Form",
                "1"
        );
        
        Signer signer = new Signer()
            .roleName("signer")
            .recipientId("1")
            .routingOrder("1");

        signer.tabs(new Tabs()
            .checkboxTabs(List.of(
                new Checkbox()
                    .documentId("1")
                    .tabLabel("Yes")
                    .anchorString("/SMS/")
                    .anchorUnits("pixels")
                    .anchorXOffset("20")
                    .anchorYOffset("10")
            ))
            .signHereTabs(List.of(
                new SignHere()
                    .documentId("1")
                    .tabLabel("Signature")
                    .anchorString("/SignHere/")
                    .anchorUnits("pixels")
                    .anchorXOffset("20")
                    .anchorYOffset("10")
            ))
            .textTabs(List.of(
                new Text()
                    .documentId("1")
                    .tabLabel("FullName")
                    .anchorString("/FullName/")
                    .anchorUnits("pixels")
                    .anchorXOffset("20")
                    .anchorYOffset("10"),
                new Text()
                    .documentId("1")
                    .tabLabel("PhoneNumber")
                    .anchorString("/PhoneNumber/")
                    .anchorUnits("pixels")
                    .anchorXOffset("20")
                    .anchorYOffset("10"),
                new Text()
                    .documentId("1")
                    .tabLabel("Company")
                    .anchorString("/Company/")
                    .anchorUnits("pixels")
                    .anchorXOffset("20")
                    .anchorYOffset("10"),
                new Text()
                    .documentId("1")
                    .tabLabel("JobTitle")
                    .anchorString("/Title/")
                    .anchorUnits("pixels")
                    .anchorXOffset("20")
                    .anchorYOffset("10")
            ))
            .dateSignedTabs(List.of(
                new DateSigned()
                    .documentId("1")
                    .tabLabel("DateSigned")
                    .anchorString("/Date/")
                    .anchorUnits("pixels")
                    .anchorXOffset("20")
                    .anchorYOffset("10")
            ))
        );

        Recipients recipients = new Recipients()
            .signers(List.of(signer));
        
        return new EnvelopeTemplate()
            .description("Example template created via the API")
            .name(templateName)
            .shared("false")
            .documents(List.of(document))
            .emailSubject("Please sign this document")
            .recipients(recipients)
            .status("created");
    }
}
