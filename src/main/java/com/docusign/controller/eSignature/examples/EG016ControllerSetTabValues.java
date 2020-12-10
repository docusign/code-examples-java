package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;

import com.docusign.esign.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/eg016")
public class EG016ControllerSetTabValues extends AbstractEsignatureController {

    private static final String DOCUMENT_FILE_NAME = "World_Wide_Corp_salary.docx";
    private static final String DOCUMENT_NAME = "Lorem Ipsum";
    private static final int ANCHOR_OFFSET_Y = 20;
    private static final int ANCHOR_OFFSET_X = 10;
    private static final String SIGNER_CLIENT_ID = "1000";

    private final Session session;
    private final User user;


    @Autowired
    public EG016ControllerSetTabValues(DSConfiguration config, Session session, User user) {
        super(config, "eg016", "Set envelope tab values");
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model,
                            HttpServletResponse response) throws ApiException, IOException {
        String signerName = args.getSignerName();
        String signerEmail = args.getSignerEmail();
        String accountId = session.getAccountId();


        EnvelopeDefinition envelope = makeEnvelope(signerEmail, signerName);

        // Step 2. Construct your API headers
        ApiClient apiClient = createApiClient(session.getBasePath(), user.getAccessToken());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

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
        // Embedded signing . There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign signing web page
        // (not the DocuSign server) to send pings via AJAX to your app.
        // NOTE: The pings will only be sent if the pingUrl is an https address
        viewRequest.setPingFrequency("600"); // seconds
        viewRequest.setPingUrl(config.getDsPingUrl());

        return viewRequest;
    }

    // Step 4. Construct your request body
    private static EnvelopeDefinition makeEnvelope(String signerEmail, String signerName) throws IOException {
        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(SIGNER_CLIENT_ID);
        signer.recipientId("1");

        // Step 3. Create tabs and CustomFields
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
        textFamiliar.setAnchorString("/familar/");
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

        Text textSalary = new Text();
        textSalary.setAnchorString("/salary/");
        textSalary.setAnchorUnits("pixels");
        textSalary.setAnchorYOffset("-9");
        textSalary.setAnchorXOffset("5");
        textSalary.setFont("helvetica");
        textSalary.setFontSize("size11");
        textSalary.setBold("true");
        textSalary.setValue(String.format("$ %d", salary));
        textSalary.setLocked("true");
        textSalary.setTabId("salary");
        textSalary.setTabLabel("salary");

        TextCustomField salaryCustomField = new TextCustomField();
        salaryCustomField.setName("salary");
        salaryCustomField.setRequired("false");
        salaryCustomField.setShow("true"); // Yes, included in the CoC
        salaryCustomField.setValue(String.valueOf(salary));

        CustomFields cf = new CustomFields();
        cf.setTextCustomFields(Arrays.asList(salaryCustomField));

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(Arrays.asList(signHere));
        tabs.setTextTabs(Arrays.asList(textLegal, textFamiliar, textSalary));

        signer.setTabs(tabs);

        // Add the recipient to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));

        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document from the Java SDK");
        envelopeDefinition.setRecipients(recipients);
        Document doc = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, "3");
        envelopeDefinition.setDocuments(Arrays.asList(doc));
        envelopeDefinition.setCustomFields(cf);
        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelopeDefinition.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);

        return envelopeDefinition;
    }
}
