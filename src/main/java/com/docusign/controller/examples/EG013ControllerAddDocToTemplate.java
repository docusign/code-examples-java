package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.sun.jersey.core.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/eg013")
public class EG013ControllerAddDocToTemplate extends EGController {

    // The id of the signer within this application.
    private static final String signerClientId = "1000";

    @Autowired
    private HttpSession session;

    @Override
    protected void addSpecialAttributes(ModelMap model) {
        model.addAttribute("templateOk", null != session.getAttribute("templateId"));
    }

    @Override
    protected String getEgName() {
        return "eg013";
    }

    @Override
    protected String getTitle() {
        return "Embedded Signing Ceremony from template and extra doc";
    }

    @Override
    protected String getResponseTitle() {
        return null;
    }

    @Override
    // ***DS.snippet.0.start
    protected EnvelopeDocumentsResult doWork(WorkArguments args, ModelMap model,
                                             String accessToken, String basePath) throws ApiException {
        // Data for this method
        // accessToken    (argument)
        // basePath       (argument)
        // config.appUrl  (url of the application itself)
        // signerClientId (class constant)
        String accountId = args.getAccountId();


        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        // Step 1. Make the envelope request body
        // prepare arguments
        args.setDsReturnUrl(config.appUrl + "/ds-return");
        args.setDsPingUrl(config.appUrl + "/");
        args.setSignerClientId(signerClientId);
        EnvelopeDefinition envelope = makeEnvelope(args);

        // Step 2. call Envelopes::create API method
        // Exceptions will be caught by the calling function
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelope);
        // process results
        String envelopeId = results.getEnvelopeId();
        System.out.println("Envelope was created. EnvelopeId " + envelopeId);

        // Step 3. create the recipient view, the Signing Ceremony
        RecipientViewRequest viewRequest = makeRecipientViewRequest(args);
        ViewUrl results1 = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);
        args.setRedirectUrl("redirect:" + results1.getUrl());
        return null;
    }

    private RecipientViewRequest makeRecipientViewRequest(WorkArguments args) {
        // Data for this method
        String returnUrl = args.getDsReturnUrl();
        String signerEmail = args.getSignerEmail();
        String signerName = args.getSignerName();
        String signerClientId = args.getSignerClientId();
        String pingUrl = args.getDsPingUrl();


        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        viewRequest.setReturnUrl(returnUrl);
        // How has your app authenticated the user? In addition to your app's
        // authentication, you can include authenticate steps from DocuSign.
        // Eg, SMS authentication
        viewRequest.setAuthenticationMethod("none");
        // Recipient information must match embedded recipient info
        // we used to create the envelope.
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(signerClientId);

        // DocuSign recommends that you redirect to DocuSign for the
        // Signing Ceremony. There are multiple ways to save state.
        // To maintain your application's session, use the pingUrl
        // parameter. It causes the DocuSign Signing Ceremony web page
        // (not the DocuSign server) to send pings via AJAX to your
        // app,
        viewRequest.setPingFrequency("600"); // seconds
        // NOTE: The pings will only be sent if the pingUrl is an https address
        viewRequest.setPingUrl(pingUrl); // optional setting

        return viewRequest;
    }

    private EnvelopeDefinition makeEnvelope(WorkArguments args) {
        // Data for this method
        String signerEmail = args.getSignerEmail();
        String signerName = args.getSignerName();
        String ccEmail = args.getCcEmail();
        String ccName = args.getCcName();
        String signerClientId = args.getSignerClientId();
        String templateId = args.getTemplateId();

        // The envelope request object uses Composite Template to
        // include in the envelope:
        // 1. A template stored on the DocuSign service
        // 2. An additional document which is a custom HTML source document

        // Create Recipients for server template. Note that Recipients object
        // is used, not TemplateRole
        //
        // Create a signer recipient for the signer role of the server template
        Signer signer1 = new Signer();
        signer1.setEmail(signerEmail);
        signer1.setName(signerName);
        signer1.setRoleName("signer");
        signer1.setRecipientId("1");
        // Adding clientUserId transforms the template recipient
        // into an embedded recipient:
        signer1.setClientUserId(signerClientId);
        // Create the cc recipient
        CarbonCopy cc1 = new CarbonCopy();
        cc1.setEmail(ccEmail);
        cc1.setName(ccName);
        cc1.setRoleName("cc");
        cc1.setRecipientId("2");
        // Recipients object:
        Recipients recipientsServerTemplate = new Recipients();
        recipientsServerTemplate.setCarbonCopies(Arrays.asList(cc1));
        recipientsServerTemplate.setSigners(Arrays.asList(signer1));

        // create a composite template for the Server Template
        CompositeTemplate compTemplate1 = new CompositeTemplate();
        compTemplate1.setCompositeTemplateId("1");
        ServerTemplate serverTemplates = new ServerTemplate();
        serverTemplates.setSequence("1");
        serverTemplates.setTemplateId(templateId);

        compTemplate1.setServerTemplates(Arrays.asList(serverTemplates));
        // Add the roles via an inlineTemplate
        InlineTemplate inlineTemplate = new InlineTemplate();
        inlineTemplate.setSequence("1");
        inlineTemplate.setRecipients(recipientsServerTemplate);
        compTemplate1.setInlineTemplates(Arrays.asList(inlineTemplate));
        // The signer recipient for the added document with
        // a tab definition:
        SignHere signHere1 = new SignHere();
        signHere1.setAnchorString("**signature_1**");
        signHere1.setAnchorYOffset("10");
        signHere1.setAnchorUnits("pixels");
        signHere1.setAnchorXOffset("20");

        Tabs signer1Tabs = new Tabs();
        signer1Tabs.setSignHereTabs(Arrays.asList(signHere1));
        // Signer definition for the added document
        Signer signer1AddedDoc = new Signer();
        signer1AddedDoc.setEmail(signerEmail);
        signer1AddedDoc.setName(signerName);
        signer1AddedDoc.setClientUserId(signerClientId);
        signer1AddedDoc.setRoleName("signer");
        signer1AddedDoc.setRecipientId("1");
        signer1AddedDoc.setTabs(signer1Tabs);
        // Recipients object for the added document:
        Recipients recipientsAddedDoc = new Recipients();
        recipientsAddedDoc.carbonCopies(Arrays.asList(cc1));
        recipientsAddedDoc.signers(Arrays.asList(signer1AddedDoc));

        // create the HTML document
        Document doc1 = new Document();

        String doc1b64 = new String(Base64.encode(document1(args)));
        doc1.setDocumentBase64(doc1b64);
        doc1.setName("Appendix 1--Sales order"); // can be different from actual file name
        doc1.setFileExtension("html");
        doc1.setDocumentId("1");
        // create a composite template for the added document
        CompositeTemplate compTemplate2 = new CompositeTemplate();
        compTemplate2.setCompositeTemplateId("2");
        // Add the recipients via an inlineTemplate
        InlineTemplate inlineTemplate2 = new InlineTemplate();
        inlineTemplate2.setSequence("2");
        inlineTemplate2.setRecipients(recipientsAddedDoc);
        compTemplate2.setInlineTemplates(Arrays.asList(inlineTemplate2));
        compTemplate2.setDocument(doc1);

        EnvelopeDefinition env = new EnvelopeDefinition();
        env.setStatus("sent");
        env.setCompositeTemplates(Arrays.asList(compTemplate1, compTemplate2));

        return env;
    }

    private String document1(WorkArguments args) {
        // Data for this method
        String signerEmail = args.getSignerEmail();
        String signerName = args.getSignerName();
        String ccEmail = args.getCcEmail();
        String ccName = args.getCcName();
        String item = args.getItem();
        String quantity = args.getQuantity();


        return " <!DOCTYPE html>\n" +
                "    <html>\n" +
                "        <head>\n" +
                "          <meta charset=\"UTF-8\">\n" +
                "        </head>\n" +
                "        <body style=\"font-family:sans-serif;margin-left:2em;\">\n" +
                "        <h1 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif;\n" +
                "            color: darkblue;margin-bottom: 0;\">World Wide Corp</h1>\n" +
                "        <h2 style=\"font-family: 'Trebuchet MS', Helvetica, sans-serif;\n" +
                "          margin-top: 0px;margin-bottom: 3.5em;font-size: 1em;\n" +
                "          color: darkblue;\">Order Processing Division</h2>\n" +
                "        <h4>Ordered by " + signerName + "</h4>\n" +
                "        <p style=\"margin-top:0em; margin-bottom:0em;\">Email: " + signerEmail + "</p>\n" +
                "        <p style=\"margin-top:0em; margin-bottom:0em;\">Copy to: " + ccName + "," + ccEmail + "</p>\n" +
                "        <p style=\"margin-top:3em; margin-bottom:0em;\">Item: <b>" + item + "</b>, quantity: <b>" + quantity + "</b> at market price.</p>\n" +
                "        <p style=\"margin-top:3em;\">\n" +
                "  Candy bonbon pastry jujubes lollipop wafer biscuit biscuit. Topping brownie sesame snaps sweet roll pie. Croissant danish biscuit soufflé caramels jujubes jelly. Dragée danish caramels lemon drops dragée. Gummi bears cupcake biscuit tiramisu sugar plum pastry. Dragée gummies applicake pudding liquorice. Donut jujubes oat cake jelly-o. Dessert bear claw chocolate cake gummies lollipop sugar plum ice cream gummies cheesecake.\n" +
                "        </p>\n" +
                "        <!-- Note the anchor tag for the signature field is in white. -->\n" +
                "        <h3 style=\"margin-top:3em;\">Agreed: <span style=\"color:white;\">**signature_1**/</span></h3>\n" +
                "        </body>\n" +
                "    </html>";
    }
    // ***DS.snippet.0.end
}
