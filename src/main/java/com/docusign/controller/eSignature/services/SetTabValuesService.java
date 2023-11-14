package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SetTabValuesService {
    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_salary.docx";

    private static final String DOCUMENT_NAME = "Lorem Ipsum";

    private static final String SIGNER_CLIENT_ID = "1000";

    public static EnvelopeSummary createEnvelope(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelope);

        return envelopeSummary;
    }

    public static ViewUrl createRecipientView(
            EnvelopesApi envelopesApi,
            String accountId,
            String envelopeId,
            RecipientViewRequest viewRequest
    ) throws ApiException {
        return envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
    }

    public static RecipientViewRequest makeRecipientViewRequest(
            String signerEmail,
            String signerName,
            String dsReturnUrl,
            String dsPingUrl
    ) {
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        // The query parameter is included as an example of how
        // to save/recover state information during the redirect to
        // the DocuSign signing. It's usually better to use
        // the session mechanism of your web framework. Query parameters
        // can be changed/spoofed very easily.
        String state = "?state=123";
        viewRequest.setReturnUrl(dsReturnUrl + state);

        return setEmailAndSignerNameForRecipientViewRequest(
                viewRequest,
                signerEmail,
                signerName,
                dsPingUrl);
    }

    public static RecipientViewRequest setEmailAndSignerNameForRecipientViewRequest(
            RecipientViewRequest viewRequest,
            String signerEmail,
            String signerName,
            String dsPingUrl
    ) {
        // How has your app authenticated the user? In addition to your app's
        // authentication, you can include authenticate steps from DocuSign.
        // Eg, SMS authentication
        String authenticationMethod = "none";
        viewRequest.setAuthenticationMethod(authenticationMethod);

        // Recipient information must match embedded recipient info
        // we used to create the envelope.
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(SIGNER_CLIENT_ID);

        // DocuSign recommends that you redirect to DocuSign for the
        // Embedded signing . There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign signing web page
        // (not the DocuSign server) to send pings via AJAX to your app.
        // NOTE: The pings will only be sent if the pingUrl is an https address
        String pingFrequency = "600";
        viewRequest.setPingFrequency(pingFrequency); // seconds
        viewRequest.setPingUrl(dsPingUrl);

        return viewRequest;
    }

    // Construct your request body
    //ds-snippet-start:eSign16Step3
    public static EnvelopeDefinition makeEnvelope(String signerEmail, String signerName) throws IOException {
        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(SIGNER_CLIENT_ID);
        signer.recipientId("1");

        // Create tabs and CustomFields
        SignHere signHere = new SignHere();
        signHere.setAnchorString("/sn1");
        signHere.setAnchorUnits("pixels");
        signHere.setAnchorYOffset("10");
        signHere.setAnchorXOffset("20");

        Text textLegal = new Text();
        textLegal.setAnchorString("/legal/");
        textLegal.setAnchorUnits("pixels");
        textLegal.setAnchorYOffset("-9");
        textLegal.setAnchorXOffset("5");
        textLegal.setFont("helvetica");
        textLegal.setFontSize("size11");
        textLegal.setBold("true");
        textLegal.setValue(signerName);
        textLegal.setLocked("false");
        textLegal.setTabId("legal_name");
        textLegal.setTabLabel("Legal Name");

        Text textFamiliar = new Text();
        textFamiliar.setAnchorString("/familiar/");
        textFamiliar.setAnchorUnits("pixels");
        textFamiliar.setAnchorYOffset("-9");
        textFamiliar.setAnchorXOffset("5");
        textFamiliar.setFont("helvetica");
        textFamiliar.setFontSize("size11");
        textFamiliar.setBold("true");
        textFamiliar.setValue(signerName);
        textFamiliar.setLocked("false");
        textFamiliar.setTabId("familiar_name");
        textFamiliar.setTabLabel("Familiar Name");

        Integer salary = 123000;

        Numerical numericalSalary = new Numerical();
        numericalSalary.setValidationType("Currency");
        numericalSalary.setPageNumber("1");
        numericalSalary.setDocumentId("1");
        numericalSalary.setXPosition("210");
        numericalSalary.setYPosition("235");
        numericalSalary.setHeight("20");
        numericalSalary.setWidth("70");
        numericalSalary.setFont("helvetica");
        numericalSalary.setFontSize("size11");
        numericalSalary.setBold("true");
        numericalSalary.setNumericalValue(String.format("%d", salary));
        numericalSalary.setTabId("salary");
        numericalSalary.setTabLabel("salary");

        LocalePolicyTab localePolicyTab = new LocalePolicyTab();
        localePolicyTab.setCultureName("en-US");
        localePolicyTab.setCurrencyCode("usd");
        localePolicyTab.setCurrencyPositiveFormat("csym_1_comma_234_comma_567_period_89");
        localePolicyTab.setCurrencyNegativeFormat("minus_csym_1_comma_234_comma_567_period_89");
        localePolicyTab.setUseLongCurrencyFormat("true");
        numericalSalary.setLocalePolicy(localePolicyTab);

        TextCustomField salaryCustomField = new TextCustomField();
        salaryCustomField.setName("salary");
        salaryCustomField.setRequired("false");
        salaryCustomField.setShow("true"); // Yes, included in the CoC
        salaryCustomField.setValue(String.valueOf(salary));

        CustomFields cf = new CustomFields();
        cf.setTextCustomFields(Collections.singletonList(salaryCustomField));

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Collections.singletonList(signHere));
        tabs.setTextTabs(Arrays.asList(textLegal, textFamiliar));
        tabs.setNumericalTabs(List.of(numericalSalary));

        signer.setTabs(tabs);

        // Add the recipient to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(Collections.singletonList(signer));

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document from the Java SDK");
        envelopeDefinition.setRecipients(recipients);
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "1");
        envelopeDefinition.setDocuments(Collections.singletonList(doc));
        envelopeDefinition.setCustomFields(cf);
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
    }
    //ds-snippet-end:eSign16Step3
}
