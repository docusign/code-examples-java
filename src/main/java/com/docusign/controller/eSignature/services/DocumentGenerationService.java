package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Document generation code example
 */
public final class DocumentGenerationService {

    public static final String CANDIDATE_NAME = "Candidate_Name";

    public static final String MANAGER_NAME = "Manager_Name";

    public static final String JOB_TITLE = "Job_Title";

    public static final String SALARY = "Salary";

    public static final String START_DATE = "Start_Date";

    public static final String TEXT_BOX = "TextBox";

    public static final String STRING_TRUE = "true";

    public String generateDocument(
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

        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(
                accountId,
                makeEnvelope(candidateEmail, candidateName, template.getTemplateId()));
        String envelopeId = envelopeSummary.getEnvelopeId();

        EnvelopeDocumentsResult documents = envelopesApi.listDocuments(accountId, envelopeId);

        String documentId = "";
        if (!documents.getEnvelopeDocuments().isEmpty()) {
            EnvelopeDocument document = documents.getEnvelopeDocuments().get(0);
            if (document != null){
                documentId = document.getDocumentIdGuid();
            }
        }

        DocGenFormFieldRequest formFields = getFormFields(
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

    private DocGenFormFieldRequest getFormFields(
            String documentId,
            String candidateName,
            String managerName,
            String jobTitle,
            String salary,
            String startDate) {
        DocGenFormField candidateNameField = new DocGenFormField();
        candidateNameField.setLabel(CANDIDATE_NAME);
        candidateNameField.setRequired(STRING_TRUE);
        candidateNameField.setType(TEXT_BOX);
        candidateNameField.setName(CANDIDATE_NAME);
        candidateNameField.setValue(candidateName);

        DocGenFormField managerNameField = new DocGenFormField();
        managerNameField.setLabel(MANAGER_NAME);
        managerNameField.setRequired(STRING_TRUE);
        managerNameField.setType(TEXT_BOX);
        managerNameField.setName(MANAGER_NAME);
        managerNameField.setValue(managerName);

        DocGenFormField jobTitleField = new DocGenFormField();
        jobTitleField.setLabel(JOB_TITLE);
        jobTitleField.setRequired(STRING_TRUE);
        jobTitleField.setType(TEXT_BOX);
        jobTitleField.setName(JOB_TITLE);
        jobTitleField.setValue(jobTitle);

        DocGenFormField salaryField = new DocGenFormField();
        salaryField.setLabel(SALARY);
        salaryField.setRequired(STRING_TRUE);
        salaryField.setType(TEXT_BOX);
        salaryField.setName(SALARY);
        salaryField.setValue(salary);

        DocGenFormField startDateField = new DocGenFormField();
        startDateField.setLabel(START_DATE);
        startDateField.setRequired(STRING_TRUE);
        startDateField.setType(TEXT_BOX);
        startDateField.setName(START_DATE);
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

    private EnvelopeDefinition makeEnvelope(String candidateEmail, String candidateName, String templateId) {
        TemplateRole signerRole = new TemplateRole();
        signerRole.setName(candidateName);
        signerRole.setEmail(candidateEmail);
        signerRole.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setTemplateRoles(Collections.singletonList(signerRole));
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);
        envelopeDefinition.setTemplateId(templateId);

        return envelopeDefinition;
    }

    private EnvelopeTemplate makeTemplate(String offerDocDocx) throws IOException {
        String documentName = "Offer Letter Demo";
        Document document = EnvelopeHelpers.createDocumentFromFile(offerDocDocx, documentName,"1");
        document.setOrder("1");
        document.pages("1");
        document.setIsDocGenDocument("true");

        DocGenFormField candidateName = new DocGenFormField();
        candidateName.setLabel(CANDIDATE_NAME);
        candidateName.setRequired(STRING_TRUE);
        candidateName.setType(TEXT_BOX);
        candidateName.setName(CANDIDATE_NAME);

        DocGenFormField managerName = new DocGenFormField();
        managerName.setLabel(MANAGER_NAME);
        managerName.setRequired(STRING_TRUE);
        managerName.setType(TEXT_BOX);
        managerName.setName(MANAGER_NAME);

        DocGenFormField jobTitle = new DocGenFormField();
        jobTitle.setLabel(JOB_TITLE);
        jobTitle.setRequired(STRING_TRUE);
        jobTitle.setType(TEXT_BOX);
        jobTitle.setName(JOB_TITLE);

        DocGenFormField salary = new DocGenFormField();
        salary.setLabel(SALARY);
        salary.setRequired(STRING_TRUE);
        salary.setType(TEXT_BOX);
        salary.setName(SALARY);

        DocGenFormField startDate = new DocGenFormField();
        startDate.setLabel(START_DATE);
        startDate.setRequired(STRING_TRUE);
        startDate.setType(TEXT_BOX);
        startDate.setName(START_DATE);

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

    private SignHere createSignHere() {
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
