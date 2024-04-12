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

    public static final String BONUS = "Bonus";

    public static final String RSUS = "RSUs";

    public static final String START_DATE = "Start_Date";

    public static final String COMPENSATION_PACKAGE = "Compensation_Package";

    public static final String COMPENSATION_COMPONENT = "Compensation_Component";

    public static final String DETAILS = "Details";

    public static final String TEXT_BOX = "TextBox";

    public static final String STRING_TRUE = "true";

    public static final String ANCHOR_UNITS = "pixels";

    public static final String DEFAULT_ID = "1";

    //ds-snippet-start:eSign42Step3
    private static EnvelopeDefinition addDocumentTemplate(String offerDocDocx) throws IOException {
        String documentName = "OfferLetterDemo.docx";
        Document document = EnvelopeHelpers.createDocumentFromFile(offerDocDocx, documentName, DEFAULT_ID);
        document.setOrder("1");
        document.pages("1");

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setDocuments(Collections.singletonList(document));

        return envelopeDefinition;
    }
    //ds-snippet-end:eSign42Step3

    public String generateDocument(
            String accountId,
            String candidateEmail,
            String candidateName,
            String managerName,
            String jobTitle,
            String salary,
            String rsus,
            String startDate,
            String offerDocDocx,
            EnvelopesApi envelopesApi,
            TemplatesApi templatesApi
    ) throws ApiException, IOException {
        //ds-snippet-start:eSign42Step2
        TemplateSummary template = templatesApi.createTemplate(accountId, makeTemplate());
        String templateId = template.getTemplateId();
        //ds-snippet-end:eSign42Step2

        //ds-snippet-start:eSign42Step3
        templatesApi.updateDocument(accountId, templateId, DEFAULT_ID, addDocumentTemplate(offerDocDocx));
        //ds-snippet-end:eSign42Step3

        //ds-snippet-start:eSign42Step4
        templatesApi.createTabs(accountId, templateId, DEFAULT_ID, prepareTabs());
        //ds-snippet-end:eSign42Step4

        //ds-snippet-start:eSign42Step5
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(
                accountId,
                makeEnvelope(candidateEmail, candidateName, template.getTemplateId()));
        String envelopeId = envelopeSummary.getEnvelopeId();
        //ds-snippet-end:eSign42Step5

        //ds-snippet-start:eSign42Step6
        DocGenFormFieldResponse formFieldResponse = envelopesApi.getEnvelopeDocGenFormFields(accountId, envelopeId);
        String documentId = "";
        if (!formFieldResponse.getDocGenFormFields().isEmpty()) {
            DocGenFormFields docGenFormFields = formFieldResponse.getDocGenFormFields().get(0);
            if (docGenFormFields != null) {
                documentId = docGenFormFields.getDocumentId();
            }
        }
        //ds-snippet-end:eSign42Step6

        //ds-snippet-start:eSign42Step7
        DocGenFormFieldRequest formFields = getFormFields(
                documentId,
                candidateName,
                managerName,
                jobTitle,
                salary,
                rsus,
                startDate);

        envelopesApi.updateEnvelopeDocGenFormFields(accountId, envelopeId, formFields);
        //ds-snippet-end:eSign42Step7

        //ds-snippet-start:eSign42Step8
        Envelope envelope = new Envelope();
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        EnvelopeUpdateSummary envelopeUpdateSummary = envelopesApi.update(accountId, envelopeId, envelope);
        //ds-snippet-end:eSign42Step8
        return envelopeUpdateSummary.getEnvelopeId();
    }

    //ds-snippet-start:eSign42Step4
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

    private DateSigned createDateSigned() {
      DateSigned dateSigned = new DateSigned();

      dateSigned.setAnchorString("Date Signed");
      dateSigned.setAnchorUnits(ANCHOR_UNITS);
      dateSigned.setAnchorYOffset("-22");

      return dateSigned;
    }
    //ds-snippet-end:eSign42Step4

    //ds-snippet-start:eSign42Step7
    private DocGenFormFieldRequest getFormFields(
            String documentId,
            String candidateName,
            String managerName,
            String jobTitle,
            String salary,
            String rsus,
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

        DocGenFormField startDateField = new DocGenFormField();
        startDateField.setName(START_DATE);
        startDateField.setValue(startDate);

        DocGenFormField compensationPackageField = new DocGenFormField();
        compensationPackageField.setName(COMPENSATION_PACKAGE);
        compensationPackageField.setType("TableRow");
        compensationPackageField.setRowValues(Arrays.asList(
            new DocGenFormFieldRowValue()
                .docGenFormFieldList(Arrays.asList(
                    new DocGenFormField()
                        .name(COMPENSATION_COMPONENT)
                        .value(SALARY),
                    new DocGenFormField()
                        .name(DETAILS)
                        .value("$" + salary)
                )),
            new DocGenFormFieldRowValue()
                .docGenFormFieldList(Arrays.asList(
                    new DocGenFormField()
                        .name(COMPENSATION_COMPONENT)
                        .value(BONUS),
                    new DocGenFormField()
                        .name(DETAILS)
                        .value("20%")
                )),
            new DocGenFormFieldRowValue()
                .docGenFormFieldList(Arrays.asList(
                    new DocGenFormField()
                        .name(COMPENSATION_COMPONENT)
                        .value(RSUS),
                    new DocGenFormField()
                        .name(DETAILS)
                        .value(rsus)
                ))
        ));

        DocGenFormFields formFields = new DocGenFormFields();
        formFields.setDocGenFormFieldList(Arrays.asList(
                candidateNameField,
                managerNameField,
                jobTitleField,
                startDateField,
                compensationPackageField));
        formFields.setDocumentId(documentId);

        DocGenFormFieldRequest docGenFormFieldRequest = new DocGenFormFieldRequest();
        docGenFormFieldRequest.setDocGenFormFields(Collections.singletonList(formFields));

        return docGenFormFieldRequest;
    }
    //ds-snippet-end:eSign42Step7

    //ds-snippet-start:eSign42Step5
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
    //ds-snippet-end:eSign42Step5

    //ds-snippet-start:eSign42Step2
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
    //ds-snippet-end:eSign42Step2
}
