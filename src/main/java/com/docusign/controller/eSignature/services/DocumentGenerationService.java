package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public final class DocumentGenerationService {
    public static String documentGeneration(
            String accountId,
            String candidateEmail,
            String candidateName,
            String managerName,
            String jobTitle,
            String salary,
            String startDate,
            String offerDocDocx,
            EnvelopesApi envelopesApi,
            TemplatesApi templatesApi
    ) throws ApiException, IOException {
        TemplateSummary template = templatesApi.createTemplate(accountId, makeTemplate(offerDocDocx));
        String templateId = template.getTemplateId();

        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(
                accountId,
                makeEnvelope(candidateEmail, candidateName, templateId)
        );
        String envelopeId = envelopeSummary.getEnvelopeId();

        EnvelopeDocumentsResult documents = envelopesApi.listDocuments(accountId, envelopeId);
        String documentId = documents.getEnvelopeDocuments().stream().findFirst().get().getDocumentIdGuid();

        DocGenFormFieldRequest formFields = formFields(
                documentId,
                candidateName,
                managerName,
                jobTitle,
                salary,
                startDate);

        EnvelopesApi.UpdateEnvelopeDocGenFormFieldsOptions options = envelopesApi.new UpdateEnvelopeDocGenFormFieldsOptions();
        options.setUpdateDocgenFormfieldsOnly("false");

        envelopesApi.updateEnvelopeDocGenFormFields(accountId, envelopeId, formFields, options);

        Envelope envelope = new Envelope();
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        EnvelopeUpdateSummary envelopeUpdateSummary = envelopesApi.update(accountId, envelopeId, envelope);

        return envelopeUpdateSummary.getEnvelopeId();
    }

    public static DocGenFormFieldRequest formFields(
            String documentId,
            String candidateName,
            String managerName,
            String jobTitle,
            String salary,
            String startDate) {
        DocGenFormField candidateNameField = new DocGenFormField();
        candidateNameField.setLabel("Candidate_Name");
        candidateNameField.setRequired("true");
        candidateNameField.setType("TextBox");
        candidateNameField.setName("Candidate_Name");
        candidateNameField.setValue(candidateName);

        DocGenFormField managerNameField = new DocGenFormField();
        managerNameField.setLabel("Manager_Name");
        managerNameField.setRequired("true");
        managerNameField.setType("TextBox");
        managerNameField.setName("Manager_Name");
        managerNameField.setValue(managerName);

        DocGenFormField jobTitleField = new DocGenFormField();
        jobTitleField.setLabel("Job_Title");
        jobTitleField.setRequired("true");
        jobTitleField.setType("TextBox");
        jobTitleField.setName("Job_Title");
        jobTitleField.setValue(jobTitle);

        DocGenFormField salaryField = new DocGenFormField();
        salaryField.setLabel("Salary");
        salaryField.setRequired("true");
        salaryField.setType("TextBox");
        salaryField.setName("Salary");
        salaryField.setValue(salary);

        DocGenFormField startDateField = new DocGenFormField();
        startDateField.setLabel("Start_Date");
        startDateField.setRequired("true");
        startDateField.setType("TextBox");
        startDateField.setName("Start_Date");
        startDateField.setValue(startDate);

        DocGenFormFields formFields = new DocGenFormFields();
        formFields.setDocGenFormFieldList(Arrays.asList(
                candidateNameField,
                managerNameField,
                jobTitleField,
                salaryField,
                startDateField));
        formFields.setDocumentId(documentId);

        DocGenFormFieldRequest docGenFormFieldRequest = new DocGenFormFieldRequest();
        docGenFormFieldRequest.setDocGenFormFields(Collections.singletonList(formFields));

        return docGenFormFieldRequest;
    }

    public static EnvelopeDefinition makeEnvelope(String candidateEmail, String candidateName, String templateId) {
        TemplateRole signer = new TemplateRole();
        signer.setName(candidateName);
        signer.setEmail(candidateEmail);
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setTemplateRoles(Collections.singletonList(signer));
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);
        envelopeDefinition.setTemplateId(templateId);

        return envelopeDefinition;
    }

    public static EnvelopeTemplate makeTemplate(String offerDocDocx) throws IOException {
        String documentName = "Offer Letter Demo";
        Document document = EnvelopeHelpers.createDocumentFromFile(offerDocDocx, documentName,"1");
        document.setOrder("1");
        document.pages("1");
        document.setIsDocGenDocument("true");

        DocGenFormField candidateName = new DocGenFormField();
        candidateName.setLabel("Candidate_Name");
        candidateName.setRequired("true");
        candidateName.setType("TextBox");
        candidateName.setName("Candidate_Name");

        DocGenFormField managerName = new DocGenFormField();
        managerName.setLabel("Manager_Name");
        managerName.setRequired("true");
        managerName.setType("TextBox");
        managerName.setName("Manager_Name");

        DocGenFormField jobTitle = new DocGenFormField();
        jobTitle.setLabel("Job_Title");
        jobTitle.setRequired("true");
        jobTitle.setType("TextBox");
        jobTitle.setName("Job_Title");

        DocGenFormField salary = new DocGenFormField();
        salary.setLabel("Salary");
        salary.setRequired("true");
        salary.setType("TextBox");
        salary.setName("Salary");

        DocGenFormField startDate = new DocGenFormField();
        startDate.setLabel("Start_Date");
        startDate.setRequired("true");
        startDate.setType("TextBox");
        startDate.setName("Start_Date");

        document.setDocGenFormFields(Arrays.asList(candidateName, managerName, jobTitle, salary, startDate));

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Collections.singletonList(createSignHere()));
        tabs.setDateSignedTabs(Collections.singletonList(createDateSigned()));

        Signer signer = new Signer();
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(tabs);

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeTemplate template = new EnvelopeTemplate();
        template.setDocuments(Collections.singletonList(document));
        template.setEmailSubject("Please sign this document");
        template.setName("Example Template");
        template.setDescription("Example template created via the API");
        template.setRecipients(recipients);
        template.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);

        return template;
    }

    public static SignHere createSignHere() {
        SignHere signHere = new SignHere();

        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setXPosition("75");
        signHere.setYPosition("415");

        return signHere;
    }

    public static DateSigned createDateSigned() {
        DateSigned dateSigned = new DateSigned();

        dateSigned.setDocumentId("1");
        dateSigned.setPageNumber("1");
        dateSigned.setXPosition("290");
        dateSigned.setYPosition("435");

        return dateSigned;
    }
}
