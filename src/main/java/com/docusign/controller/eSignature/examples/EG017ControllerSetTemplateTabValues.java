package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.docusign.esign.model.Checkbox;
import com.docusign.esign.model.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Controller
@RequestMapping("/eg017")
public class EG017ControllerSetTemplateTabValues extends AbstractEsignatureController {


    private static final String MODEL_LIST_TEMPLATE = "listTemplates";
    private static final String SIGNER_CLIENT_ID = "1000";

    private final Session session;
    private final User user;


    @Autowired
    public EG017ControllerSetTemplateTabValues(DSConfiguration config, Session session, User user) {
        super(config, "eg017", "Set template tab values");
        this.session = session;
        this.user = user;
    }

    @Override
    protected void onInitModel(WorkArguments args, ModelMap model) throws Exception {
        super.onInitModel(args, model);
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        TemplatesApi templatesApi = new TemplatesApi(apiClient);
        EnvelopeTemplateResults templates = templatesApi.listTemplates(session.getAccountId());
        model.addAttribute(MODEL_LIST_TEMPLATE, templates.getEnvelopeTemplates());
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String accountId = session.getAccountId();
        String ccName = args.getCcName();
        String ccEmail = args.getCcEmail();
        String templateId = args.getTemplateId();


        // Step 2. Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        
        // Step 3. is shown in the makeEnvelope method below
        // Step 4. Construct your request body
        EnvelopeDefinition envelope = makeEnvelope(signerEmail, signerName, ccEmail,  ccName, templateId);

        // Step 5. Call the eSignature REST API
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelope);
        String envelopeId = envelopeSummary.getEnvelopeId();
        session.setEnvelopeId(envelopeId);

        // Step 6. Create the view request
        RecipientViewRequest viewRequest = makeRecipientViewRequest(signerEmail, signerName);
        ViewUrl viewUrl = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);

        // State can be stored/recovered using the framework's session or a
        // query parameter on the returnUrl (see the makeRecipientViewRequest method)
        return new RedirectView(viewUrl.getUrl());
    }

    private RecipientViewRequest makeRecipientViewRequest(String signerEmail, String signerName) {
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        // The query parameter is included as an example of how
        // to save/recover state information during the redirect to
        // the DocuSign signing. It's usually better to use
        // the session mechanism of your web framework. Query parameters
        // can be changed/spoofed very easily.
        viewRequest.setReturnUrl(config.getDsReturnUrl() + "?state=123");

        // How has your app authenticated the user? In addition to your app's
        // authentication, you can include authenticate steps from DocuSign.
        // Eg, SMS authentication
        viewRequest.setAuthenticationMethod("none");

        // Recipient information must match embedded recipient info
        // we used to create the envelope.
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(SIGNER_CLIENT_ID);

        // DocuSign recommends that you redirect to DocuSign for the
        // embedded signing. There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign signing web page
        // (not the DocuSign server) to send pings via AJAX to your app.
        // NOTE: The pings will only be sent if the pingUrl is an https address
        viewRequest.setPingFrequency("600"); // seconds
        viewRequest.setPingUrl(config.getDsPingUrl());

        return viewRequest;
    }


    private static EnvelopeDefinition makeEnvelope(String signerEmail, String signerName, String ccEmail, String ccName, String templateId){
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
        signer.setClientUserId(SIGNER_CLIENT_ID);
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
