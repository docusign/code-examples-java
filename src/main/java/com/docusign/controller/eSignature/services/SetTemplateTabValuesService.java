package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.util.Arrays;
import java.util.Collections;

public final class SetTemplateTabValuesService {
    public static ViewUrl setTemplateTabValues(
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
            String clientUserId,
            String dsPingUrl
    ) {
        //ds-snippet-start:eSign17Step6
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

        return SetTabValuesService.setEmailAndSignerNameForRecipientViewRequest(
                viewRequest,
                signerEmail,
                signerName,
                dsPingUrl);
        //ds-snippet-end:eSign17Step6
    }

    public static EnvelopeDefinition makeEnvelope(
            String signerEmail,
            String signerName,
            String ccEmail,
            String ccName,
            String templateId,
            String clientUserId
    ) {
        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient

        // Create tabs and CustomFields
        //ds-snippet-start:eSign17Step3
        List list1 = new List();
        list1.setValue("green");
        list1.setDocumentId("1");
        list1.setPageNumber("1");
        list1.setTabLabel("list");

        // Checkboxes
        Checkbox ck1 = new Checkbox();
        ck1.setTabLabel("ckAuthorization");
        ck1.setSelected("true");

        Checkbox ck2 = new Checkbox();
        ck2.setTabLabel("ckAgreement");
        ck2.setSelected("true");

        Radio radio = new Radio();
        radio.setValue("white");
        radio.setSelected("true");

        RadioGroup rg = new RadioGroup();
        rg.setGroupName("radio1");
        rg.setRadios(Collections.singletonList(radio));

        Text txt = new Text();
        txt.setTabLabel("text");
        txt.setValue("Jabberywocky!");

        // We can also add a new tab (field) to the ones already in the template:

        Text txtExtra = new Text();
        txtExtra.setDocumentId("1");
        txtExtra.setPageNumber("1");
        txtExtra.setXPosition("280");
        txtExtra.setYPosition("172");
        txtExtra.setFont("helvetica");
        txtExtra.setFontSize("size14");
        txtExtra.setTabLabel("added text field");
        txtExtra.setHeight("23");
        txtExtra.setWidth("84");
        txtExtra.setRequired("false");
        txtExtra.setBold("true");
        txtExtra.setValue(signerName);
        txtExtra.setLocked("false");
        txtExtra.setTabId("name");

        // Add the tabs model (including the SignHere tab) to the signer.
        // The Tabs object wants arrays of the different field/tab types
        // Tabs are set per recipient / signer

        Tabs tabs = new Tabs();

        tabs.setTextTabs(Arrays.asList(txt, txtExtra));
        tabs.setRadioGroupTabs(Collections.singletonList(rg));
        tabs.setCheckboxTabs(Arrays.asList(ck1, ck2));
        tabs.setListTabs(Collections.singletonList(list1));

        // create a signer recipient to sign the document, identified by name and email
        // We're setting the parameters via the object creation
        TemplateRole signer = new TemplateRole();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        // Setting the client_user_id marks the signer as embedded
        signer.setClientUserId(clientUserId);
        signer.setRoleName("signer");
        signer.setTabs(tabs);

        TemplateRole cc = new TemplateRole();
        cc.setEmail(ccEmail);
        cc.setName(ccName);
        cc.setRoleName("cc");

        // Create an envelope custom field to save our application"s
        // Data about the envelope
        TextCustomField customField = new TextCustomField();
        customField.setName("app metadata item");
        customField.setValue("1234567");
        customField.setRequired("false");
        customField.show("true"); //Yes, include in the CoC

        CustomFields cf = new CustomFields();
        cf.setTextCustomFields(Collections.singletonList(customField));
        //ds-snippet-end:eSign17Step3

        // Next, create the top level envelope definition and populate it.
        //ds-snippet-start:eSign17Step4
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document from the Java SDK");
        // The Recipients object wants arrays for each recipient type
        envelopeDefinition.setTemplateId(templateId);
        envelopeDefinition.setTemplateRoles(Arrays.asList(signer, cc));
        envelopeDefinition.setCustomFields(cf);
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
        //ds-snippet-end:eSign17Step4
    }
}
