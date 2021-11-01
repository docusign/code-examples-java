package com.docusign.services.eSignature.examples;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.util.Arrays;

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
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        // The query parameter is included as an example of how
        // to save/recover state information during the redirect to
        // the DocuSign signing. It's usually better to use
        // the session mechanism of your web framework. Query parameters
        // can be changed/spoofed very easily.
        viewRequest.setReturnUrl(dsReturnUrl + "?state=123");

        // How has your app authenticated the user? In addition to your app's
        // authentication, you can include authenticate steps from DocuSign.
        // Eg, SMS authentication
        viewRequest.setAuthenticationMethod("none");

        // Recipient information must match embedded recipient info
        // we used to create the envelope.
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(clientUserId);

        // DocuSign recommends that you redirect to DocuSign for the
        // embedded signing. There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign signing web page
        // (not the DocuSign server) to send pings via AJAX to your app.
        // NOTE: The pings will only be sent if the pingUrl is an https address
        viewRequest.setPingFrequency("600"); // seconds
        viewRequest.setPingUrl(dsPingUrl);

        return viewRequest;
    }

    public static EnvelopeDefinition makeEnvelope(
        String signerEmail,
        String signerName,
        String ccEmail,
        String ccName,
        String templateId,
        String clientUserId
    ){
        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient

        // Step 3. Create tabs and CustomFields
        List list1 = new List();
        list1.setValue("green");
        list1.setDocumentId("1");
        list1.setPageNumber("1");
        list1.setTabLabel("list");

        // Checkboxes
        Checkbox ck1 =  new Checkbox();
        ck1.setTabLabel("ckAuthorization");
        ck1.setSelected("true");

        Checkbox ck2 =  new Checkbox();
        ck2.setTabLabel("ckAgreement");
        ck2.setSelected("true");

        Radio radio = new Radio();
        radio.setValue("white");
        radio.setSelected("true");

        RadioGroup rg = new RadioGroup();
        rg.setGroupName("radio1");
        rg.setRadios(Arrays.asList(radio));


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
        tabs.setRadioGroupTabs(Arrays.asList(rg));
        tabs.setCheckboxTabs(Arrays.asList(ck1, ck2));
        tabs.setListTabs(Arrays.asList(list1));


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
        cf.setTextCustomFields(Arrays.asList(customField));


        // Next, create the top level envelope definition and populate it.
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
    }
}
