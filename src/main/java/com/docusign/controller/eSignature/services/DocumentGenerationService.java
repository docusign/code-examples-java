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
        TemplateSummary template = templatesApi.createTemplate(accountId, makeTemplate());
        String templateId = template.getTemplateId();

        templatesApi.updateDocument(accountId, templateId, "1", addDocumentTemplate(offerDocDocx));
        templatesApi.createTabs(accountId, templateId, "1", prepareTabs());

        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(
                accountId,
                makeEnvelope(candidateEmail, candidateName, template.getTemplateId()));
        String envelopeId = envelopeSummary.getEnvelopeId();

        DocGenFormFieldResponse formFieldResponse = envelopesApi.getEnvelopeDocGenFormFields(accountId, envelopeId);
        String documentId = "";
        if (!formFieldResponse.getDocGenFormFields().isEmpty()) {
            DocGenFormFields docGenFormFields = formFieldResponse.getDocGenFormFields().get(0);
            if (docGenFormFields != null){
                documentId = docGenFormFields.getDocumentId();
            }
        }

        DocGenFormFieldRequest formFields = getFormFields(
                documentId,
                candidateName,
                managerName,
                jobTitle,
                salary,
                startDate);

        envelopesApi.updateEnvelopeDocGenFormFields(accountId, envelopeId, formFields);

        Envelope envelope = new Envelope();
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        EnvelopeUpdateSummary envelopeUpdateSummary = envelopesApi.update(accountId, envelopeId, envelope);

        return envelopeUpdateSummary.getEnvelopeId();
    }

    private TemplateTabs prepareTabs() {
        SignHere signHere = createSignHere();
        DateSigned dateSigned = createDateSigned();

        TemplateTabs templateTabs = new TemplateTabs();
        templateTabs.setSignHereTabs(Collections.singletonList(signHere));
        templateTabs.setDateSignedTabs(Collections.singletonList(dateSigned));

        return templateTabs;
    }

    private SignHere createSignHere() {
        SignHere signHere = new SignHere();

        signHere.setAnchorString("Employee Signature");
        signHere.setAnchorUnits("pixels");
        signHere.setAnchorXOffset("5");
        signHere.setAnchorYOffset("-22");

        return signHere;
    }

    public static DateSigned createDateSigned() {
        DateSigned dateSigned = new DateSigned();

        dateSigned.setAnchorString("Date");
        dateSigned.setAnchorUnits("pixels");
        dateSigned.setAnchorYOffset("-22");

        return dateSigned;
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

    private EnvelopeTemplate makeTemplate() {
        Signer signer = new Signer();
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");

        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeTemplate template = new EnvelopeTemplate();
        template.setEmailSubject("Please sign this document");
        template.setName("Example Template");
        template.setDescription("Example template created via the API");
        template.setRecipients(recipients);
        template.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);

        return template;
    }

    private static EnvelopeDefinition addDocumentTemplate(String offerDocDocx) throws IOException {
        String documentName = "OfferLetterDemo.docx";
        Document document = EnvelopeHelpers.createDocumentFromFile(offerDocDocx, documentName,"1");
        document.setOrder("1");
        document.pages("1");

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setDocuments(Collections.singletonList(document));

        return envelopeDefinition;
    }
}
