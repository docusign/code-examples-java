package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.DocGenFormFieldResponse;
import com.docusign.esign.model.DocGenFormFields;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.TemplateSummary;
import com.docusign.esign.model.DocGenFormFieldRequest;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeUpdateSummary;
import com.docusign.esign.model.TemplateTabs;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.DateSigned;
import com.docusign.esign.model.DocGenFormField;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.TemplateRole;
import com.docusign.esign.model.EnvelopeTemplate;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Document;

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
    public static final String ANCHOR_UNITS = "pixels";
    public static final String DEFAULT_ID = "1";

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
        // Step 2a start
        TemplateSummary template = templatesApi.createTemplate(accountId, makeTemplate());
        String templateId = template.getTemplateId();
        // Step 2a end

        // Step 3a start
        templatesApi.updateDocument(accountId, templateId, DEFAULT_ID, addDocumentTemplate(offerDocDocx));
        // Step 3a end
        
        // Step 4a start
        templatesApi.createTabs(accountId, templateId, DEFAULT_ID, prepareTabs());
        // Step 4a end

        // Step 5a start
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(
                accountId,
                makeEnvelope(candidateEmail, candidateName, template.getTemplateId()));
        String envelopeId = envelopeSummary.getEnvelopeId();
        // Step 5a end

        // Step 6 start
        DocGenFormFieldResponse formFieldResponse = envelopesApi.getEnvelopeDocGenFormFields(accountId, envelopeId);
        String documentId = "";
        if (!formFieldResponse.getDocGenFormFields().isEmpty()) {
            DocGenFormFields docGenFormFields = formFieldResponse.getDocGenFormFields().get(0);
            if (docGenFormFields != null){
                documentId = docGenFormFields.getDocumentId();
            }
        }
        // Step 6 end
        // Step 7a start
        DocGenFormFieldRequest formFields = getFormFields(
                documentId,
                candidateName,
                managerName,
                jobTitle,
                salary,
                startDate);

        envelopesApi.updateEnvelopeDocGenFormFields(accountId, envelopeId, formFields);
        // Step 7a end

        // Step 8 start
        Envelope envelope = new Envelope();
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        EnvelopeUpdateSummary envelopeUpdateSummary = envelopesApi.update(accountId, envelopeId, envelope);
        // Step 8 end
        return envelopeUpdateSummary.getEnvelopeId();
    }

    // Step 4b start
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
        signHere.setAnchorUnits(ANCHOR_UNITS);
        signHere.setAnchorXOffset("5");
        signHere.setAnchorYOffset("-22");

        return signHere;
    }

    public static DateSigned createDateSigned() {
        DateSigned dateSigned = new DateSigned();

        dateSigned.setAnchorString("Date");
        dateSigned.setAnchorUnits(ANCHOR_UNITS);
        dateSigned.setAnchorYOffset("-22");

        return dateSigned;
    }
        // Step 4b end

    // Step 7b start
    private DocGenFormFieldRequest getFormFields(
            String documentId,
            String candidateName,
            String managerName,
            String jobTitle,
            String salary,
            String startDate) {
        DocGenFormField candidateNameField = new DocGenFormField();
        candidateNameField.setName(CANDIDATE_NAME);
        candidateNameField.setValue(candidateName);

        DocGenFormField managerNameField = new DocGenFormField();
        managerNameField.setName(MANAGER_NAME);
        managerNameField.setValue(managerName);

        DocGenFormField jobTitleField = new DocGenFormField();
        jobTitleField.setName(JOB_TITLE);
        jobTitleField.setValue(jobTitle);

        DocGenFormField salaryField = new DocGenFormField();
        salaryField.setName(SALARY);
        salaryField.setValue(salary);

        DocGenFormField startDateField = new DocGenFormField();
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
    // Step 7b end

    // Step 5b start
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
    // Step 5b end

    // Step 2b start
    private EnvelopeTemplate makeTemplate() {
        Signer signer = new Signer();
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId(DEFAULT_ID);
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
    // Step 2b end

    // Step 3b start
    private static EnvelopeDefinition addDocumentTemplate(String offerDocDocx) throws IOException {
        String documentName = "OfferLetterDemo.docx";
        Document document = EnvelopeHelpers.createDocumentFromFile(offerDocDocx, documentName,DEFAULT_ID);
        document.setOrder("1");
        document.pages("1");

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setDocuments(Collections.singletonList(document));

        return envelopeDefinition;
    }
    // Step 3b stop
}
