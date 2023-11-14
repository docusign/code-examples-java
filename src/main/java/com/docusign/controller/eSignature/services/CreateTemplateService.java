package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public final class CreateTemplateService {
    private static final String PDF_DOCUMENT_FILE_NAME = "World_Wide_Corp_fields.pdf";

    private static final String PDF_DOCUMENT_NAME = "Lorem Ipsum";

    private static final String DOCUMENT_ID = "1";

    private static final String PAGE_NUMBER = "1";

    private static final String FALSE = "false";

    public static TemplateSummary createTemplate(
            ApiClient apiClient,
            String accountId,
            EnvelopeTemplate envelopeTemplate
    ) throws ApiException {
        TemplatesApi templatesApi = new TemplatesApi(apiClient);
        return templatesApi.createTemplate(accountId, envelopeTemplate);
    }

    public static EnvelopeTemplateResults searchTemplatesByName(
            ApiClient apiClient,
            String accountId,
            String templateName
    ) throws ApiException {
        TemplatesApi templatesApi = new TemplatesApi(apiClient);
        TemplatesApi.ListTemplatesOptions options = templatesApi.new ListTemplatesOptions();
        options.setSearchText(templateName);
        return templatesApi.listTemplates(accountId, options);
    }

    // document 1 (pdf) has tag /sn1/
    //
    // The template has two recipient roles.
    // recipient 1 - signer
    // recipient 2 - cc
    // The template will be sent first to the signer.
    // After it is signed, a copy is sent to the cc person.
    //ds-snippet-start:eSign8Step2
    public static EnvelopeTemplate makeTemplate(String templateName) throws IOException {
        Document doc = EnvelopeHelpers.createDocumentFromFile(PDF_DOCUMENT_FILE_NAME, PDF_DOCUMENT_NAME, "1");

        // Tabs are set per recipient / signer
        Tabs signer1Tabs = new Tabs();
        signer1Tabs.setCheckboxTabs(Arrays.asList(
                createCheckbox("ckAuthorization", "75", "417"),
                createCheckbox("ckAuthentication", "75", "447"),
                createCheckbox("ckAgreement", "75", "478"),
                createCheckbox("ckAcknowledgement", "75", "508")));
        signer1Tabs.setListTabs(Collections.singletonList(createList()));
        signer1Tabs.setRadioGroupTabs(Collections.singletonList(createRadioGroup()));
        signer1Tabs.setSignHereTabs(Collections.singletonList(createSignHere()));
        signer1Tabs.textTabs(java.util.List.of(
                createText("text", "153", "230")));
        signer1Tabs.numericalTabs(java.util.List.of(
                createNumerical("numericalCurrency", "153", "260")));

        // create a signer recipient to sign the document, identified by name and email
        // routingOrder (lower means earlier) determines the order of deliveries
        // to the recipients. Parallel routing order is supported by using the
        // same integer as the order for two or more recipients.
        Signer signer = new Signer();
        signer.setRoleName(EnvelopeHelpers.SIGNER_ROLE_NAME);
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signer1Tabs);

        // create a cc recipient to receive a copy of the documents, identified by name and email
        CarbonCopy cc1 = new CarbonCopy();
        cc1.setRoleName(EnvelopeHelpers.CC_ROLE_NAME);
        cc1.setRoutingOrder("2");
        cc1.setRecipientId("2");

        // Create the overall template definition. The order in the docs array
        // determines the order in the envelope.
        EnvelopeTemplate template = new EnvelopeTemplate();
        template.setDocuments(Collections.singletonList(doc));
        template.setEmailSubject("Please sign this document");
        template.setName(templateName);
        template.setDescription("Example template created via the API");
        template.setShared(FALSE);
        template.setRecipients(EnvelopeHelpers.createRecipients(signer, cc1));
        template.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_CREATED);

        return template;
    }

    public static List createList() {
        List list = new List();
        list.setDocumentId(DOCUMENT_ID);
        list.setPageNumber(PAGE_NUMBER);
        list.setXPosition("142");
        list.setYPosition("291");
        list.setFont("helvetica");
        list.setFontSize("size14");
        list.setTabLabel("list");
        list.setRequired(FALSE);
        list.setListItems(Arrays.asList(
                createListItem("Red"),
                createListItem("Orange"),
                createListItem("Yellow"),
                createListItem("Green"),
                createListItem("Blue"),
                createListItem("Indigo"),
                createListItem("Violet")
        ));
        return list;
    }

    public static ListItem createListItem(String color) {
        ListItem item = new ListItem();
        item.setText(color);
        item.setValue(color.toLowerCase(Locale.ENGLISH));
        return item;
    }

    public static RadioGroup createRadioGroup() {
        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setDocumentId(DOCUMENT_ID);
        radioGroup.setGroupName("radio1");

        radioGroup.setRadios(Arrays.asList(
                createRadio("white", "142"),
                createRadio("red", "74"),
                createRadio("blue", "220")
        ));
        return radioGroup;
    }

    public static Radio createRadio(String value, String xPosition) {
        Radio radio = new Radio();
        radio.setPageNumber(PAGE_NUMBER);
        radio.setValue(value);
        radio.setXPosition(xPosition);
        radio.setYPosition("384");
        radio.setRequired(FALSE);
        return radio;
    }

    public static Checkbox createCheckbox(String label, String xPosition, String yPosition) {
        Checkbox check = new Checkbox();
        check.setDocumentId(DOCUMENT_ID);
        check.setPageNumber(PAGE_NUMBER);
        check.setXPosition(xPosition);
        check.setYPosition(yPosition);
        check.setTabLabel(label);
        return check;
    }

    public static Text createText(String label, String xPosition, String yPosition) {
        Text text = new Text();
        text.setDocumentId(DOCUMENT_ID);
        text.setPageNumber(PAGE_NUMBER);
        text.setXPosition(xPosition);
        text.setYPosition(yPosition);
        text.setFont("helvetica");
        text.setFontSize("size14");
        text.setTabLabel(label);
        text.setHeight("23");
        text.setWidth("84");
        text.setRequired(FALSE);
        return text;
    }

    public static Numerical createNumerical(String label, String xPosition, String yPosition) {
        Numerical numerical = new Numerical();
        numerical.setValidationType("Currency");
        numerical.setDocumentId(DOCUMENT_ID);
        numerical.setPageNumber(PAGE_NUMBER);
        numerical.setXPosition(xPosition);
        numerical.setYPosition(yPosition);
        numerical.setFont("helvetica");
        numerical.setFontSize("size14");
        numerical.setTabLabel(label);
        numerical.setHeight("23");
        numerical.setWidth("84");
        numerical.setRequired(FALSE);
        return numerical;
    }

    public static SignHere createSignHere() {
        SignHere signHere = new SignHere();
        signHere.setDocumentId(DOCUMENT_ID);
        signHere.setPageNumber(PAGE_NUMBER);
        signHere.setXPosition("191");
        signHere.setYPosition("148");
        return signHere;
    }
    //ds-snippet-end:eSign8Step2
}
